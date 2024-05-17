package com.github.romanqed.devspark.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.Response;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.Permissions;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import javalinjwt.JWTProvider;
import javalinjwt.JavalinJWT;

public class AuthBase {
    protected final JWTProvider<JwtUser> provider;
    protected final Repository<User> users;

    protected AuthBase(JWTProvider<JwtUser> provider, Repository<User> users) {
        this.provider = provider;
        this.users = users;
    }

    protected DecodedJWT auth(Context ctx) {
        var decoded = JavalinJWT.getTokenFromHeader(ctx).flatMap(provider::validateToken);
        if (decoded.isEmpty()) {
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.json(new Response("Missing or invalid token"));
            return null;
        }
        return decoded.get();
    }

    protected String getUserId(Context ctx) {
        var decoded = JavalinJWT.getTokenFromHeader(ctx).flatMap(provider::validateToken);
        return decoded
                .map(decodedJWT -> decodedJWT.getClaim("id").asString())
                .orElse(null);
    }

    protected String getCheckedUserId(Context ctx) {
        var decoded = auth(ctx);
        if (decoded == null) {
            return null;
        }
        return decoded.getClaim("id").asString();
    }

    protected User getUser(Context ctx) {
        var decoded = JavalinJWT.getTokenFromHeader(ctx).flatMap(provider::validateToken);
        return decoded
                .map(decodedJWT -> users.get(decodedJWT.getClaim("id").asString()))
                .orElse(null);
    }

    protected User getCheckedUser(Context ctx) {
        var id = getCheckedUserId(ctx);
        if (id == null) {
            return null;
        }
        var ret = users.get(id);
        if (ret == null) {
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.json(new Response("User not found"));
            return null;
        }
        if (ret.isBanned()) {
            ctx.status(HttpStatus.FORBIDDEN);
            ctx.json(new Response("User banned"));
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
            return false;
        }
        return true;
    }
}
