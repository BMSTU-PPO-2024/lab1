package com.github.romanqed.devspark.dto;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public final class Pagination {
    private static final int DEFAULT_BATCH = 10;

    private final int page;
    private final int batch;

    public Pagination(int page) {
        this.page = page;
        this.batch = DEFAULT_BATCH;
    }

    public Pagination(int page, int batch) {
        this.page = page;
        this.batch = batch;
    }

    private static int parse(String raw, int def) {
        if (raw == null) {
            return def;
        }
        var ret = Integer.parseInt(raw);
        if (ret < 1) {
            throw new IllegalArgumentException("Invalid value");
        }
        return ret;
    }

    public static Pagination from(Context ctx) {
        try {
            var page = parse(ctx.queryParam("page"), 1);
            var batch = parse(ctx.queryParam("batch"), DEFAULT_BATCH);
            return new Pagination(page, batch);
        } catch (Throwable e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new Response(e.getMessage()));
            return null;
        }
    }

    public int getPage() {
        return page;
    }

    public int getBatch() {
        return batch;
    }
}
