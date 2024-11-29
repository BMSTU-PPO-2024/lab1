package com.github.romanqed.devspark.database;

public interface DatabaseFactory {
    Database create(String name, Iterable<Class<?>> classes);
}
