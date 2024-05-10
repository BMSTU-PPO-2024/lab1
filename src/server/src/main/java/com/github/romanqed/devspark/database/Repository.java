package com.github.romanqed.devspark.database;

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

    long deleteFirst(F filter);

    long deleteAll(F filter);

    long deleteAll(Iterable<K> keys);
}
