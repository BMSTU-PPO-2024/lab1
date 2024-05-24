package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.DtoUtil;
import com.github.romanqed.devspark.dto.PostDto;
import com.github.romanqed.devspark.dto.Response;
import com.github.romanqed.devspark.dto.TextDto;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtProvider;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.*;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;

import java.util.Date;

@JavalinController("/post")
public final class PostController extends AuthBase {
    private final Repository<Tag> tags;
    private final Repository<Post> posts;
    private final Repository<Comment> comments;

    public PostController(JwtProvider<JwtUser> provider,
                          Repository<User> users,
                          Repository<Post> posts,
                          Repository<Comment> comments,
                          Repository<Tag> tags) {
        super(provider, users);
        this.posts = posts;
        this.comments = comments;
        this.tags = tags;
    }

    @Route(method = HandlerType.GET, route = "/{postId}")
    public void get(Context ctx) {
        var user = getUser(ctx);
        var post = Util.see(ctx, user, "postId", posts);
        if (post == null) {
            return;
        }
        ctx.json(post);
    }

    @Route(method = HandlerType.GET, route = "/{postId}/comments")
    public void listComments(Context ctx) {
        var pagination = DtoUtil.parsePagination(ctx);
        if (pagination == null) {
            return;
        }
        var user = getUser(ctx);
        var post = Util.see(ctx, user, "postId", posts);
        if (post == null) {
            return;
        }
        ctx.json(post.retrieveComments(comments, pagination));
    }

    @Route(method = HandlerType.PUT, route = "/{postId}/comment")
    public void publishComment(Context ctx) {
        var dto = DtoUtil.validate(ctx, TextDto.class);
        if (dto == null) {
            return;
        }
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        var post = Util.see(ctx, user, "postId", posts);
        if (post == null) {
            return;
        }
        var comment = Comment.of(user.getId(), post.getId(), dto.getText());
        comments.put(comment);
        ctx.json(comment);
    }

    private boolean updatePost(Context ctx, PostDto dto, Post post) {
        var ret = false;
        // Update title
        var title = dto.getTitle();
        if (title != null) {
            ret = true;
            post.setTitle(title);
        }
        // Update text
        var text = dto.getText();
        if (text != null) {
            ret = true;
            post.setText(text);
        }
        // Update privacy
        var privacy = dto.getVisible();
        if (privacy != null) {
            ret = true;
            post.setVisible(privacy);
        }
        // Update tagIds
        var tagIds = dto.getTagIds();
        if (tagIds != null) {
            ret = true;
            if (!tags.exists(tagIds)) {
                ctx.status(HttpStatus.NOT_FOUND);
                ctx.json(new Response("Invalid tag ids"));
                return false;
            }
            post.setTagIds(tagIds);
        }
        if (!ret) {
            ctx.status(HttpStatus.BAD_REQUEST);
        }
        return ret;
    }

    @Route(method = HandlerType.PATCH, route = "/{postId}")
    public void update(Context ctx) {
        var dto = DtoUtil.parse(ctx, PostDto.class);
        if (dto == null) {
            return;
        }
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        var post = posts.get(ctx.pathParam("postId"));
        if (post == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        if (!post.isOwnedBy(user) && !user.hasPermission(Permissions.MANAGE_POSTS)) {
            ctx.status(HttpStatus.FORBIDDEN);
            return;
        }
        if (!updatePost(ctx, dto, post)) {
            return;
        }
        post.setUpdated(new Date());
        posts.update(post.getId(), post);
    }

    private void deletePost(Context ctx, String id) {
        if (!Post.delete(comments, id, posts)) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        ctx.status(HttpStatus.OK);
    }

    @Route(method = HandlerType.DELETE, route = "/{postId}")
    public void delete(Context ctx) {
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        var id = ctx.pathParam("postId");
        if (user.hasPermission(Permissions.MANAGE_POSTS)) {
            deletePost(ctx, id);
            return;
        }
        if (!Post.delete(posts, comments, user.getId(), id)) {
            ctx.status(HttpStatus.FORBIDDEN);
            return;
        }
        ctx.status(HttpStatus.OK);
    }

    @Route(method = HandlerType.PUT, route = "/{postId}/rate")
    public void addRate(Context ctx) {
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        var post = Util.see(ctx, user, "postId", posts);
        if (post == null) {
            return;
        }
        Util.rate(ctx, user, post, posts);
    }

    @Route(method = HandlerType.DELETE, route = "/{postId}/rate")
    public void deleteRate(Context ctx) {
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        var post = Util.see(ctx, user, "postId", posts);
        if (post == null) {
            return;
        }
        Util.unrate(ctx, user, post, posts);
    }
}
