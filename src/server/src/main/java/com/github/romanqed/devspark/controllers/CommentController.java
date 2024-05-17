package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.CommentDto;
import com.github.romanqed.devspark.dto.DtoUtil;
import com.github.romanqed.devspark.dto.TextDto;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.Comment;
import com.github.romanqed.devspark.models.Permissions;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import javalinjwt.JWTProvider;

import java.util.Date;

@JavalinController("/comment")
public final class CommentController extends AuthBase {
    private final Repository<Comment> comments;

    public CommentController(JWTProvider<JwtUser> provider, Repository<User> users, Repository<Comment> comments) {
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
        ctx.json(CommentDto.of(comment));
    }

    private boolean cannotManipulate(Context ctx, Comment comment) {
        var user = getCheckedUser(ctx);
        if (user == null) {
            return true;
        }
        if (!comment.getOwnerId().equals(user.getId()) && !user.hasPermission(Permissions.MANAGE_COMMENTS)) {
            ctx.status(HttpStatus.FORBIDDEN);
            return true;
        }
        return false;
    }

    @Route(method = HandlerType.PATCH, route = "/{commentId}")
    public void update(Context ctx) {
        var comment = comments.get(ctx.pathParam("commentId"));
        if (comment == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        if (cannotManipulate(ctx, comment)) {
            return;
        }
        var dto = DtoUtil.parse(ctx, TextDto.class);
        if (dto == null) {
            return;
        }
        // Update text
        var text = dto.getText();
        if (text == null) {
            ctx.status(HttpStatus.BAD_REQUEST);
            return;
        }
        comment.setText(text);
        comment.setUpdated(new Date());
        comments.update(comment.getId(), comment);
    }

    @Route(method = HandlerType.DELETE, route = "/{commentId}")
    public void delete(Context ctx) {
        var comment = comments.get(ctx.pathParam("commentId"));
        if (comment == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        if (cannotManipulate(ctx, comment)) {
            return;
        }
        comments.delete(comment.getId());
    }

    @Route(method = HandlerType.PUT, route = "/{commentId}/rate")
    public void addRate(Context ctx) {
        Util.rate(ctx, "commentId", this, comments);
    }

    @Route(method = HandlerType.DELETE, route = "/{commentId}/rate")
    public void deleteRate(Context ctx) {
        Util.unrate(ctx, "commentId", this, comments);
    }
}
