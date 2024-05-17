package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.Channel;
import com.github.romanqed.devspark.models.Post;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import javalinjwt.JWTProvider;

@JavalinController("/channel")
public final class ChannelController extends AuthBase {
    private final Repository<Channel> channels;
    private final Repository<Post> posts;

    public ChannelController(JWTProvider<JwtUser> provider,
                             Repository<User> users,
                             Repository<Channel> channels,
                             Repository<Post> posts) {
        super(provider, users);
        this.channels = channels;
        this.posts = posts;
    }

    @Route(method = HandlerType.GET, route = "/{channelId}")
    public void get(Context ctx) {

    }

    @Route(method = HandlerType.GET, route = "/{channelId}/posts")
    public void listPosts(Context ctx) {

    }

    @Route(method = HandlerType.GET)
    public void find(Context ctx) {

    }

    @Route(method = HandlerType.GET, route = "/{channelId}/post")
    public void findPosts(Context ctx) {

    }

    @Route(method = HandlerType.PUT)
    public void put(Context ctx) {

    }

    @Route(method = HandlerType.PUT, route = "/{channelId}/post")
    public void publishPost(Context ctx) {

    }

    @Route(method = HandlerType.PATCH, route = "/{channelId}")
    public void update(Context ctx) {

    }

    @Route(method = HandlerType.DELETE, route = "/{channelId}")
    public void delete(Context ctx) {

    }
}
