package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.DtoUtil;
import com.github.romanqed.devspark.dto.TagDto;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.Permissions;
import com.github.romanqed.devspark.models.Tag;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import javalinjwt.JWTProvider;

import java.util.Date;

@JavalinController("/tag")
public final class TagController extends AuthBase {
    private final Repository<Tag> tags;

    public TagController(JWTProvider<JwtUser> provider, Repository<User> users, Repository<Tag> tags) {
        super(provider, users);
        this.tags = tags;
    }

    @Route(method = HandlerType.GET, route = "/{tagId}")
    public void get(Context ctx) {
        var tag = tags.get(ctx.pathParam("tagId"));
        if (tag == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        ctx.json(tag);
    }

    @Route(method = HandlerType.GET)
    public void find(Context ctx) {
        var pagination = DtoUtil.parsePagination(ctx);
        if (pagination == null) {
            return;
        }
        Util.findByName(ctx, tags, pagination);
    }

    @Route(method = HandlerType.PUT)
    public void put(Context ctx) {
        var dto = DtoUtil.validate(ctx, TagDto.class);
        if (dto == null) {
            return;
        }
        if (!validatePermission(ctx, Permissions.MANAGE_TAGS)) {
            return;
        }
        var name = dto.getName();
        if (tags.exists("name", name)) {
            ctx.status(HttpStatus.CONFLICT);
            return;
        }
        var tag = Tag.of(name);
        tags.put(tag);
        ctx.json(tag);
    }

    @Route(method = HandlerType.PATCH, route = "/{tagId}")
    public void update(Context ctx) {
        var dto = DtoUtil.validate(ctx, TagDto.class);
        if (dto == null) {
            return;
        }
        if (!validatePermission(ctx, Permissions.MANAGE_TAGS)) {
            return;
        }
        var tag = tags.get(ctx.pathParam("tagId"));
        if (tag == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        tag.setName(dto.getName());
        tag.setUpdated(new Date());
        tags.update(tag.getId(), tag);
    }

    @Route(method = HandlerType.DELETE, route = "/{tagId}")
    public void delete(Context ctx) {
        if (!validatePermission(ctx, Permissions.MANAGE_TAGS)) {
            return;
        }
        if (!tags.delete(ctx.pathParam("tagId"))) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        ctx.status(HttpStatus.OK);
    }
}
