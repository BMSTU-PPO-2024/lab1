package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.Comment;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import javalinjwt.JWTProvider;

@JavalinController("/comment")
public final class CommentController extends AuthBase {
    private final Repository<Comment> comments;

    public CommentController(JWTProvider<JwtUser> provider, Repository<User> users, Repository<Comment> comments) {
        super(provider, users);
        this.comments = comments;
    }

    @Route(method = HandlerType.GET, route = "/{commentId}")
    public void get(Context ctx) {

    }

    @Route(method = HandlerType.PATCH, route = "/{commentId}")
    public void update(Context ctx) {

    }

    @Route(method = HandlerType.DELETE, route = "/{commentId}")
    public void delete(Context ctx) {

    }

    @Route(method = HandlerType.PUT, route = "/{commentId}/rate")
    public void addRate(Context ctx) {

    }

    @Route(method = HandlerType.DELETE, route = "/{commentId}/rate")
    public void deleteRate(Context ctx) {

    }
}
