package com.github.romanqed.devspark.javalin;

import io.javalin.http.HandlerType;

import java.util.Objects;

public final class HandlerData {
    private final String route;
    private final HandlerType type;

    public HandlerData(String route, HandlerType type) {
        this.route = route;
        this.type = type;
    }

    public String getRoute() {
        return route;
    }

    public HandlerType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (HandlerData) o;
        return route.equals(that.route) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(route, type);
    }
}
