package com.github.romanqed.devspark.javalin;

import io.javalin.http.Handler;

import java.util.Map;

public interface HandlerFactory {
    Map<HandlerData, Handler> create(Object object);
}