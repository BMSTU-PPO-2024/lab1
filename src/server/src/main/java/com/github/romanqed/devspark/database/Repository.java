package com.github.romanqed.devspark.database;

import org.bson.types.ObjectId;

import java.util.Collection;

public interface Repository<K, V, F> {

    long put(V model);

    long put(Iterable<V> entities);

    long update(K key, V model);

    V find(K key);

    long delete(K key);

    Iterable<V> findAll(Iterable<K> keys);

    Iterable<V> filter(F filter);

    Iterable<V> filterAndSort(F filter, F sort);

    V findFirst(F filter);

    long count(F filter);

    boolean exists(ObjectId id);

    boolean exists(Collection<ObjectId> ids);

    long deleteFirst(F filter);

    long deleteAll(F filter);

    long deleteAll(Iterable<K> keys);
}
