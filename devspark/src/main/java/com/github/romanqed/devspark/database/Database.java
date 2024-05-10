package com.github.romanqed.devspark.database;

public interface Database<K, F> {
    <V> Repository<K, V, F> create(String name, Class<V> type);
}
