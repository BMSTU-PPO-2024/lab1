package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.DtoUtil;
import com.github.romanqed.devspark.dto.FeedDto;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.*;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import javalinjwt.JWTProvider;

import java.util.Date;

@JavalinController("/feed")
public final class FeedController extends AuthBase {
    private final Repository<Feed> feeds;
    private final Repository<Post> posts;
    private final Repository<Channel> channels;
    private final Repository<Tag> tags;

    public FeedController(JWTProvider<JwtUser> provider,
                          Repository<User> users,
                          Repository<Feed> feeds,
                          Repository<Post> posts,
                          Repository<Channel> channels,
                          Repository<Tag> tags) {
        super(provider, users);
        this.feeds = feeds;
        this.posts = posts;
        this.channels = channels;
        this.tags = tags;
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
        var pagination = DtoUtil.parsePagination(ctx);
        if (pagination == null) {
            return;
        }
        var user = getUser(ctx);
        var feed = Util.see(ctx, user, "feedId", feeds);
        if (feed == null) {
            return;
        }
        ctx.json(feed.getPosts(posts, pagination));
    }

    @Route(method = HandlerType.GET)
    public void find(Context ctx) {
        Util.findAll(ctx, this, feeds);
    }

    @Route(method = HandlerType.PUT)
    public void put(Context ctx) {
        var dto = DtoUtil.validate(ctx, FeedDto.class);
        if (dto == null) {
            return;
        }
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        var feed = Feed.of(user.getId(), dto.getName());
        var privacy = dto.getVisible();
        if (privacy != null) {
            feed.setVisible(privacy);
        }
        if (updateIds(ctx, dto, feed) == null) {
            return;
        }
        feeds.put(feed);
        ctx.json(feed);
    }

    private Integer updateIds(Context ctx, FeedDto dto, Feed feed) {
        var ret = 0;
        // Update channelIds
        var channelIds = dto.getChannelIds();
        if (channelIds != null) {
            ret += 1;
            if (!Channel.isVisible(channels, channelIds)) {
                ctx.status(HttpStatus.BAD_REQUEST);
                return null;
            }
            feed.setChannelIds(channelIds);
        }
        // Update tagIds
        var tagIds = dto.getTagIds();
        if (tagIds != null) {
            ret += 1;
            if (!tags.exists(tagIds)) {
                ctx.status(HttpStatus.NOT_FOUND);
                return null;
            }
            feed.setTagIds(tagIds);
        }
        return ret;
    }

    private boolean updateFeed(Context ctx, FeedDto dto, Feed feed) {
        // Update name
        var name = dto.getName();
        if (name != null) {
            feed.setName(name);
        }
        // Update privacy
        var privacy = dto.getVisible();
        if (privacy != null) {
            feed.setVisible(privacy);
        }
        var count = updateIds(ctx, dto, feed);
        if (count == null) {
            return false;
        }
        return name != null || privacy != null || count != 0;
    }

    @Route(method = HandlerType.PATCH, route = "/{feedId}")
    public void update(Context ctx) {
        var dto = DtoUtil.parse(ctx, FeedDto.class);
        if (dto == null) {
            return;
        }
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        var feed = feeds.get(ctx.pathParam("feedId"));
        if (feed == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        if (!feed.isOwnedBy(user) && !user.hasPermission(Permissions.MANAGE_FEEDS)) {
            ctx.status(HttpStatus.FORBIDDEN);
            return;
        }
        if (!updateFeed(ctx, dto, feed)) {
            return;
        }
        feed.setUpdated(new Date());
        feeds.update(feed.getId(), feed);
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
        if (!Feed.delete(feeds, user.getId(), id)) {
            ctx.status(HttpStatus.FORBIDDEN);
            return;
        }
        ctx.status(HttpStatus.OK);
    }
}
