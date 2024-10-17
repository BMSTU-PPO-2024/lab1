package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.Return;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.Credentials;
import com.github.romanqed.devspark.dto.DtoUtil;
import com.github.romanqed.devspark.dto.Response;
import com.github.romanqed.devspark.dto.Token;
import com.github.romanqed.devspark.hash.Encoder;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtProvider;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.User;
import io.github.amayaframework.di.Inject;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLogger;

@JavalinController
public final class AuthController {
    private final Repository<User> users;
    private final JwtProvider<JwtUser> jwt;
    private final Encoder encoder;
    private Logger logger;

    public AuthController(Repository<User> users, JwtProvider<JwtUser> jwt, Encoder encoder) {
        this.users = users;
        this.jwt = jwt;
        this.encoder = encoder;
        this.logger = new NOPLogger() {
        };
    }

    @Inject
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private String makeToken(User user) {
        var jwtUser = new JwtUser(user.getId(), user.getNickname(), user.getEmail());
        return jwt.generateToken(jwtUser);
    }

    @Route(method = HandlerType.POST, route = "/register")
    @Return(Token.class)
    public void register(Context ctx) {
        var credentials = DtoUtil.validate(ctx, Credentials.class);
        if (credentials == null) {
            return;
        }
        var email = credentials.getEmail();
        if (users.exists("email", email)) {
            ctx.status(HttpStatus.CONFLICT);
            ctx.json(new Response("User with the specified email already exists"));
            logger.debug("User with email {} already exists", email);
            return;
        }
        var hashed = encoder.encode(credentials.getPassword());
        var user = User.of(email, hashed);
        user.setNickname(email);
        users.put(user);
        var token = makeToken(user);
        logger.debug("User {} successfully registered", user.getId());
        ctx.json(new Token(token));
    }

    @Route(method = HandlerType.POST, route = "/login")
    @Return(Token.class)
    public void login(Context ctx) {
        var credentials = DtoUtil.validate(ctx, Credentials.class);
        if (credentials == null) {
            return;
        }
        var email = credentials.getEmail();
        var user = users.findFirstByField("email", email);
        if (user == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(new Response("Unknown user"));
            logger.debug("User with email {} not found", email);
            return;
        }
        if (user.isBanned()) {
            ctx.status(HttpStatus.FORBIDDEN);
            ctx.json(new Response("User banned"));
            logger.debug("User {} banned", user.getId());
            return;
        }
        if (!encoder.matches(credentials.getPassword(), user.getPassword())) {
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.json(new Response("Invalid credentials"));
            logger.debug("User {} not authorized", user.getId());
            return;
        }
        ctx.json(new Token(makeToken(user)));
        logger.debug("User {} authorized", user.getId());
    }
}
