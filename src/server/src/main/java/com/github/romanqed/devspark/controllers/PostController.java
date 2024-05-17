package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.Comment;
import com.github.romanqed.devspark.models.Post;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import javalinjwt.JWTProvider;

@JavalinController("/post")
public final class PostController extends AuthBase {
    private final Repository<Post> posts;
    private final Repository<Comment> comments;

    public PostController(JWTProvider<JwtUser> provider,
                          Repository<User> users,
                          Repository<Post> posts,
                          Repository<Comment> comments) {
        super(provider, users);
        this.posts = posts;
        this.comments = comments;
    }

    @Route(method = HandlerType.GET, route = "/{postId}")
    public void get(Context ctx) {

    }

    @Route(method = HandlerType.GET, route = "/{postId}/comments")
    public void listComments(Context ctx) {

    }

    @Route(method = HandlerType.PUT)
    public void put(Context ctx) {

    }

    @Route(method = HandlerType.PUT, route = "/{postId}/comment")
    public void publishComment(Context ctx) {

    }

    @Route(method = HandlerType.PATCH, route = "/{postId}")
    public void update(Context ctx) {

    }

    @Route(method = HandlerType.DELETE, route = "/{postId}")
    public void delete(Context ctx) {

    }

    @Route(method = HandlerType.PUT, route = "/{postId}/rate")
    public void addRate(Context ctx) {

    }

    @Route(method = HandlerType.DELETE, route = "/{postId}/rate")
    public void deleteRate(Context ctx) {

    }
}
