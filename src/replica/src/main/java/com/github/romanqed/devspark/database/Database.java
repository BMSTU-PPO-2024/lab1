package com.github.romanqed.devspark.database;

public interface Database {
    <V> Repository<V> create(String name, Class<V> type);
}
