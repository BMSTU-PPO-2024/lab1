package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.Feed;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import javalinjwt.JWTProvider;

@JavalinController
public final class FeedController extends AuthBase {
    private final Repository<Feed> feeds;

    public FeedController(JWTProvider<JwtUser> provider, Repository<User> users, Repository<Feed> feeds) {
        super(provider, users);
        this.feeds = feeds;
    }

    @Route(method = HandlerType.GET, route = "/{feedId}")
    public void get(Context ctx) {

    }

    @Route(method = HandlerType.GET, route = "/{feedId}/posts")
    public void listPosts(Context ctx) {

    }

    @Route(method = HandlerType.GET)
    public void find(Context ctx) {

    }

    @Route(method = HandlerType.PUT)
    public void put(Context ctx) {

    }

    @Route(method = HandlerType.PATCH, route = "/{feedId}")
    public void update(Context ctx) {

    }

    @Route(method = HandlerType.DELETE, route = "/{feedId}")
    public void delete(Context ctx) {

    }
}
