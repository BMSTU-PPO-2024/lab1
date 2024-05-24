package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.DtoUtil;
import com.github.romanqed.devspark.dto.Response;
import com.github.romanqed.devspark.models.*;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.github.romanqed.devspark.CollectionUtil.asList;

final class Util {
    private Util() {
    }

    static <T> List<T> findByName(Repository<T> repository,
                                  String name,
                                  Pattern pattern,
                                  Pagination pagination) {
        if (name != null) {
            return asList(repository.findByField("name", name, pagination));
        }
        if (pattern != null) {
            return asList(repository.findMatched("name", pattern, pagination));
        }
        return asList(repository.getAll(pagination));
    }

    static void findByName(Context ctx, Repository<?> repository, Pagination pagination) {
        var name = ctx.queryParam("name");
        var pattern = ctx.queryParam("pattern");
        try {
            var compiled = pattern == null ? null : Pattern.compile(pattern);
            var found = Util.findByName(repository, name, compiled, pagination);
            ctx.json(found);
        } catch (PatternSyntaxException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new Response("Invalid pattern", e));
        }
    }

    static <V extends Rated & Identified> void rate(Context ctx, User user, V entity, Repository<V> repository) {
        if (entity.rate(user, 1)) {
            repository.update(entity.getId(), entity);
            ctx.status(HttpStatus.OK);
            return;
        }
        ctx.status(HttpStatus.CONFLICT);
        ctx.json(new Response("Entity is already rated"));
    }

    static <V extends Rated & Identified> void unrate(Context ctx, User user, V entity, Repository<V> repository) {
        if (entity.unrate(user)) {
            repository.update(entity.getId(), entity);
            ctx.status(HttpStatus.OK);
            return;
        }
        ctx.status(HttpStatus.CONFLICT);
        ctx.json(new Response("Entity is not rated yet"));
    }

    static <V extends Owned & Visible> V see(Context ctx, User user, String id, Repository<V> repository) {
        var ret = repository.get(ctx.pathParam(id));
        if (ret == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return null;
        }
        if (user != null && !user.isBanned() && user.hasPermission(Permissions.IGNORE_VISIBILITY)) {
            return ret;
        }
        if (ret.isVisible() || ret.isOwnedBy(user)) {
            return ret;
        }
        ctx.status(HttpStatus.NOT_FOUND);
        return null;
    }

    static Pattern checkPattern(Context ctx, String pattern) {
        try {
            return Pattern.compile(pattern);
        } catch (PatternSyntaxException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            return null;
        }
    }

    static void findAll(Context ctx, AuthBase auth, Repository<?> repository) {
        var pagination = DtoUtil.parsePagination(ctx);
        if (pagination == null) {
            return;
        }
        var user = auth.getUser(ctx);
        var id = user == null ? null : user.getId();
        var all = user != null && !user.isBanned() && user.hasPermission(Permissions.IGNORE_VISIBILITY);
        var name = ctx.queryParam("name");
        if (name != null) {
            var found = ModelUtil.findAllByName(repository, id, name, all, pagination);
            ctx.json(found);
            return;
        }
        var raw = ctx.queryParam("pattern");
        if (raw != null) {
            var pattern = Util.checkPattern(ctx, raw);
            if (pattern == null) {
                return;
            }
            var found = ModelUtil.matchAllByName(repository, id, pattern, all, pagination);
            ctx.json(found);
            return;
        }
        ctx.json(ModelUtil.findAll(repository, id, all, pagination));
    }
}
