package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.DtoUtil;
import com.github.romanqed.devspark.dto.TopicDto;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.Permissions;
import com.github.romanqed.devspark.models.Tag;
import com.github.romanqed.devspark.models.Topic;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import javalinjwt.JWTProvider;

import java.util.Date;

@JavalinController("/topic")
public final class TopicController extends AuthBase {
    private final Repository<Topic> topics;
    private final Repository<Tag> tags;

    public TopicController(JWTProvider<JwtUser> provider,
                           Repository<User> users,
                           Repository<Topic> topics,
                           Repository<Tag> tags) {
        super(provider, users);
        this.topics = topics;
        this.tags = tags;
    }

    @Route(method = HandlerType.GET, route = "/{topicId}")
    public void get(Context ctx) {
        var topic = topics.get(ctx.pathParam("topicId"));
        if (topic == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        ctx.json(topic);
    }

    @Route(method = HandlerType.GET, route = "/{topicId}/tags")
    public void getTags(Context ctx) {
        var topic = topics.get(ctx.pathParam("topicId"));
        if (topic == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        topic.retrieveTags(tags);
        ctx.json(topic.getTags());
    }

    @Route(method = HandlerType.GET)
    public void find(Context ctx) {
        Util.findByName(ctx, topics);
    }

    @Route(method = HandlerType.PUT)
    public void put(Context ctx) {
        var dto = DtoUtil.validate(ctx, TopicDto.class);
        if (dto == null) {
            return;
        }
        if (!validatePermission(ctx, Permissions.MANAGE_TOPICS)) {
            return;
        }
        var name = dto.getName();
        if (topics.exists("name", name)) {
            ctx.status(HttpStatus.CONFLICT);
            return;
        }
        var ids = dto.getTagIds();
        if (!tags.exists(ids)) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        var topic = Topic.of(name, ids);
        topics.put(topic);
        ctx.json(topic);
    }

    @Route(method = HandlerType.PATCH, route = "/{topicId}")
    public void update(Context ctx) {
        var dto = DtoUtil.parse(ctx, TopicDto.class);
        if (dto == null) {
            return;
        }
        var name = dto.getName();
        var ids = dto.getTagIds();
        if (name == null && ids == null) {
            ctx.status(HttpStatus.BAD_REQUEST);
            return;
        }
        if (!validatePermission(ctx, Permissions.MANAGE_TOPICS)) {
            return;
        }
        var topic = topics.get(ctx.pathParam("topicId"));
        if (topic == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        // Update name
        if (name != null) {
            topic.setName(name);
        }
        // Update ids
        if (ids != null) {
            if (!tags.exists(ids)) {
                ctx.status(HttpStatus.NOT_FOUND);
                return;
            }
            topic.setTagIds(ids);
        }
        topic.setUpdated(new Date());
        topics.update(topic.getId(), topic);
    }

    @Route(method = HandlerType.DELETE, route = "/{topicId}")
    public void delete(Context ctx) {
        if (!validatePermission(ctx, Permissions.MANAGE_TOPICS)) {
            return;
        }
        if (topics.delete(ctx.pathParam("topicId")) != 1) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        ctx.status(HttpStatus.OK);
    }
}
