package com.github.romanqed.devspark.database;

public interface DatabaseFactory<K, F> {
    Database<K, F> create(String name, Iterable<Class<?>> classes);
}
