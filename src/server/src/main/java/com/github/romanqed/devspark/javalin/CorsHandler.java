package com.github.romanqed.devspark.javalin;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.Header;
import org.jetbrains.annotations.NotNull;

final class CorsHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) {
        ctx.header(Header.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    }
}
