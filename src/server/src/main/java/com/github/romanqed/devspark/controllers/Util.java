package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.Response;
import com.github.romanqed.devspark.models.Rated;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class Util {
    private Util() {
    }

    public static <T> List<T> of(Iterable<T> iterable) {
        var ret = new LinkedList<T>();
        iterable.forEach(ret::add);
        return ret;
    }

    public static <T> List<T> findByName(Repository<T> repository,
                                         String name,
                                         Pattern pattern,
                                         Pagination pagination) {
        if (name != null) {
            return of(repository.findByField("name", name, pagination));
        }
        if (pattern != null) {
            return of(repository.findMatched("name", pattern, pagination));
        }
        return of(repository.getAll(pagination));
    }

    public static void findByName(Context ctx, Repository<?> repository, Pagination pagination) {
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

    public static <V extends Rated> void rate(Context ctx, User user, String id, V entity, Repository<V> repository) {
        if (entity.rate(user, 1)) {
            repository.update(id, entity);
            ctx.status(HttpStatus.OK);
            return;
        }
        ctx.status(HttpStatus.CONFLICT);
        ctx.json(new Response("Entity is already rated"));
    }

    public static <V extends Rated> void unrate(Context ctx, User user, String id, V entity, Repository<V> repository) {
        if (entity.unrate(user)) {
            repository.update(id, entity);
            ctx.status(HttpStatus.OK);
            return;
        }
        ctx.status(HttpStatus.CONFLICT);
        ctx.json(new Response("Entity is not rated yet"));
    }
}
