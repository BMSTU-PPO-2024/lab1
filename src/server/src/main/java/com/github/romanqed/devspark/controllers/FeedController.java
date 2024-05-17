package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.Feed;
import com.github.romanqed.devspark.models.Permissions;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import javalinjwt.JWTProvider;

@JavalinController("/feed")
public final class FeedController extends AuthBase {
    private final Repository<Feed> feeds;

    public FeedController(JWTProvider<JwtUser> provider, Repository<User> users, Repository<Feed> feeds) {
        super(provider, users);
        this.feeds = feeds;
    }

    @Route(method = HandlerType.GET, route = "/{feedId}")
    public void get(Context ctx) {
        var user = getUser(ctx);
        var feed = Util.see(ctx, user, "feedId", feeds);
        if (feed == null) {
            return;
        }
        ctx.json(feed);
    }

    @Route(method = HandlerType.GET, route = "/{feedId}/posts")
    public void listPosts(Context ctx) {
        // TODO Implement list feed's posts
    }

    @Route(method = HandlerType.GET)
    public void find(Context ctx) {
        // TODO Implement feed put
    }

    @Route(method = HandlerType.PUT)
    public void put(Context ctx) {
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        // TODO
    }

    @Route(method = HandlerType.PATCH, route = "/{feedId}")
    public void update(Context ctx) {
        // TODO
    }

    private void deleteFeed(Context ctx, String id) {
        if (!feeds.delete(id)) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        ctx.status(HttpStatus.OK);
    }

    @Route(method = HandlerType.DELETE, route = "/{feedId}")
    public void delete(Context ctx) {
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        var id = ctx.pathParam("feedId");
        if (user.hasPermission(Permissions.MANAGE_FEEDS)) {
            deleteFeed(ctx, id);
            return;
        }
        if (!Feed.delete(user.getId(), id, feeds)) {
            ctx.status(HttpStatus.FORBIDDEN);
            return;
        }
        ctx.status(HttpStatus.OK);
    }
}
