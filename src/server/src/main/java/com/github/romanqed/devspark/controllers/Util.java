package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;
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

    static <V extends Rated> void rate(Context ctx, User user, String id, V entity, Repository<V> repository) {
        if (entity.rate(user, 1)) {
            repository.update(id, entity);
            ctx.status(HttpStatus.OK);
            return;
        }
        ctx.status(HttpStatus.CONFLICT);
        ctx.json(new Response("Entity is already rated"));
    }

    static <V extends Rated> void unrate(Context ctx, User user, String id, V entity, Repository<V> repository) {
        if (entity.unrate(user)) {
            repository.update(id, entity);
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
        if (ret.isOwnedBy(user) || ret.isVisible()) {
            return ret;
        }
        return null;
    }
}
