package com.github.romanqed.devspark.database;

import java.util.Collection;

public interface Repository<V> {

    long put(V model);

    long put(Iterable<V> entities);

    long update(String key, V model);

    V find(String key);

    long delete(String key);

    Iterable<V> findAll(Iterable<String> keys);

    Iterable<V> filter(Object filter);

    Iterable<V> filterAndSort(Object filter, Object sort);

    V findFirst(Object filter);

    long count(Object filter);

    boolean exists(String id);

    boolean exists(Collection<String> ids);

    long deleteFirst(Object filter);

    long deleteAll(Object filter);

    long deleteAll(Iterable<String> keys);
}
