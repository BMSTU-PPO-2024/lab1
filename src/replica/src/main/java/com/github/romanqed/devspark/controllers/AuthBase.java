package com.github.romanqed.devspark.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.Response;
import com.github.romanqed.devspark.jwt.JwtProvider;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.Permissions;
import com.github.romanqed.devspark.models.User;
import io.github.amayaframework.di.Inject;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import javalinjwt.JavalinJWT;
import kotlin.jvm.functions.Function2;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLogger;

public class AuthBase {
    protected final JwtProvider<JwtUser> provider;
    protected final Repository<User> users;
    protected Logger logger;

    protected AuthBase(JwtProvider<JwtUser> provider, Repository<User> users) {
        this.provider = provider;
        this.users = users;
        this.logger = new NOPLogger() {
        };
    }

    @Inject
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    protected DecodedJWT auth(Context ctx) {
        var decoded = JavalinJWT.getTokenFromHeader(ctx).flatMap(provider::validateToken);
        if (decoded.isEmpty()) {
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.json(new Response("Missing or invalid token"));
            logger.debug("Auth failed due to invalid token");
            return null;
        }
        return decoded.get();
    }

    protected String getCheckedUserId(Context ctx) {
        var decoded = auth(ctx);
        if (decoded == null) {
            return null;
        }
        return decoded.getClaim("id").asString();
    }

    protected User getUser(Context ctx, Function2<Repository<User>, String, User> getter) {
        var decoded = JavalinJWT.getTokenFromHeader(ctx).flatMap(provider::validateToken);
        return decoded
                .map(decodedJWT -> getter.invoke(users, decodedJWT.getClaim("id").asString()))
                .orElse(null);
    }

    protected User getUser(Context ctx) {
        return getUser(ctx, User::getAuthUser);
    }

    protected User getCheckedFullUser(Context ctx) {
        return getCheckedUser(ctx, Repository::get);
    }

    protected User getCheckedUser(Context ctx) {
        return getCheckedUser(ctx, User::getAuthUser);
    }

    protected User getCheckedUser(Context ctx, Function2<Repository<User>, String, User> getter) {
        var id = getCheckedUserId(ctx);
        if (id == null) {
            return null;
        }
        var ret = getter.invoke(users, id);
        if (ret == null) {
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.json(new Response("User not found"));
            logger.debug("User {} not found", id);
            return null;
        }
        if (ret.isBanned()) {
            ctx.status(HttpStatus.FORBIDDEN);
            ctx.json(new Response("User banned"));
            logger.debug("User {} banned", ret.getId());
            return null;
        }
        return ret;
    }

    protected boolean validatePermission(Context ctx, Permissions permission) {
        var user = getCheckedUser(ctx);
        if (user == null) {
            return false;
        }
        if (!user.hasPermission(permission)) {
            ctx.status(HttpStatus.FORBIDDEN);
            ctx.json(new Response("User has no permission"));
            logger.debug("Permission {} invalid for user {}", permission, user);
            return false;
        }
        return true;
    }
}
