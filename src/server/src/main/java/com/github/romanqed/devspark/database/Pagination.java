package com.github.romanqed.devspark.database;

public final class Pagination {
    public static final int DEFAULT_BATCH = 10;

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

    public int getPage() {
        return page;
    }

    public int getBatch() {
        return batch;
    }
}
