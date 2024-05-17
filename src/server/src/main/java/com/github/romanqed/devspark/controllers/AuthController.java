package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.Credentials;
import com.github.romanqed.devspark.dto.DtoUtil;
import com.github.romanqed.devspark.dto.Response;
import com.github.romanqed.devspark.dto.Token;
import com.github.romanqed.devspark.hash.Encoder;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import javalinjwt.JWTProvider;

@JavalinController
public final class AuthController {
    private final Repository<User> users;
    private final JWTProvider<JwtUser> jwt;
    private final Encoder encoder;

    public AuthController(Repository<User> users, JWTProvider<JwtUser> jwt, Encoder encoder) {
        this.users = users;
        this.jwt = jwt;
        this.encoder = encoder;
    }

    private String makeToken(User user) {
        var jwtUser = new JwtUser(user.getId(), user.getNickname(), user.getEmail());
        return jwt.generateToken(jwtUser);
    }

    @Route(method = HandlerType.POST, route = "/register")
    public void register(Context ctx) {
        var credentials = DtoUtil.validate(ctx, Credentials.class);
        if (credentials == null) {
            return;
        }
        var email = credentials.getEmail();
        if (users.exists("email", email)) {
            ctx.status(HttpStatus.CONFLICT);
            ctx.json(new Response("User with the specified email already exists"));
            return;
        }
        var hashed = encoder.encode(credentials.getPassword());
        var user = User.of(email, hashed);
        user.setNickname(email);
        users.put(user);
        var token = makeToken(user);
        ctx.json(new Token(token));
    }

    @Route(method = HandlerType.POST, route = "/login")
    public void login(Context ctx) {
        var credentials = DtoUtil.validate(ctx, Credentials.class);
        if (credentials == null) {
            return;
        }
        var email = credentials.getEmail();
        var user = users.findFirstByField("email", email);
        if (user == null) {
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.json(new Response("Invalid credentials"));
            return;
        }
        if (user.isBanned()) {
            ctx.status(HttpStatus.FORBIDDEN);
            ctx.json(new Response("User banned"));
            return;
        }
        if (!encoder.matches(credentials.getPassword(), user.getPassword())) {
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.json(new Response("Invalid credentials"));
            return;
        }
        ctx.json(new Token(makeToken(user)));
    }
}
