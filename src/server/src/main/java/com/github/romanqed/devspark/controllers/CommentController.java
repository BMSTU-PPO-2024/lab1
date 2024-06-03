package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.DtoUtil;
import com.github.romanqed.devspark.dto.TextDto;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtProvider;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.Comment;
import com.github.romanqed.devspark.models.Permissions;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;

import java.util.Date;

@JavalinController("/comment")
public final class CommentController extends AuthBase {
    private final Repository<Comment> comments;

    public CommentController(JwtProvider<JwtUser> provider, Repository<User> users, Repository<Comment> comments) {
        super(provider, users);
        this.comments = comments;
    }

    @Route(method = HandlerType.GET, route = "/{commentId}")
    public void get(Context ctx) {
        var comment = comments.get(ctx.pathParam("commentId"));
        if (comment == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        ctx.json(comment);
    }

    @Route(method = HandlerType.PATCH, route = "/{commentId}")
    public void update(Context ctx) {
        var dto = DtoUtil.validate(ctx, TextDto.class);
        if (dto == null) {
            return;
        }
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        var comment = comments.get(ctx.pathParam("commentId"));
        if (comment == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        if (!comment.isOwnedBy(user) && !user.hasPermission(Permissions.MANAGE_COMMENTS)) {
            ctx.status(HttpStatus.FORBIDDEN);
            return;
        }
        // Update text
        comment.setText(dto.getText());
        comment.setUpdated(new Date());
        comments.update(comment.getId(), comment);
        logger.debug("Comment {} updated", comment.getId());
    }

    private void deleteComment(Context ctx, String id) {
        if (!comments.delete(id)) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        ctx.status(HttpStatus.OK);
        logger.debug("Comment {} deleted by privileged user", id);
    }

    @Route(method = HandlerType.DELETE, route = "/{commentId}")
    public void delete(Context ctx) {
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        var id = ctx.pathParam("commentId");
        if (user.hasPermission(Permissions.MANAGE_COMMENTS)) {
            deleteComment(ctx, id);
            return;
        }
        if (!Comment.delete(comments, user.getId(), id)) {
            ctx.status(HttpStatus.FORBIDDEN);
            return;
        }
        ctx.status(HttpStatus.OK);
        logger.debug("Comment {} deleted", id);
    }

    private void doRate(Context ctx, Consumer4<Context, User, Comment, Repository<Comment>> consumer) {
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        var id = ctx.pathParam("commentId");
        var comment = comments.get(id);
        if (comment == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        consumer.consume(ctx, user, comment, comments);
    }

    @Route(method = HandlerType.PUT, route = "/{commentId}/rate")
    public void addRate(Context ctx) {
        doRate(ctx, Util::rate);
        logger.debug("Comment {} rated", ctx.pathParam("commentId"));
    }

    @Route(method = HandlerType.DELETE, route = "/{commentId}/rate")
    public void deleteRate(Context ctx) {
        doRate(ctx, Util::unrate);
        logger.debug("Comment {} unrated", ctx.pathParam("commentId"));
    }
}
