package com.github.romanqed.devspark.database;

import java.util.Collection;
import java.util.regex.Pattern;

public interface Repository<V> {

    long put(V model);

    long put(Iterable<V> entities);

    long update(String key, V model);

    V get(String key);

    long delete(String key);

    Iterable<V> getAll();

    Iterable<V> getAll(int page, int batch);

    Iterable<V> getAll(Iterable<String> keys);

    Iterable<V> getAll(Iterable<String> keys, int page, int batch);

    V findFirstByField(String field, Object value);

    Iterable<V> findByField(String field, Object value);

    Iterable<V> findByField(String field, Object value, int page, int batch);

    Iterable<V> findByField(String field, Iterable<Object> values);

    Iterable<V> findByField(String field, Iterable<Object> values, int page, int batch);

    Iterable<V> findMatched(String field, String pattern);

    Iterable<V> findMatched(String field, String pattern, int page, int batch);

    Iterable<V> findMatched(String field, Pattern pattern, int page, int batch);

    long countByField(String field, Object value);

    long countByField(String field, Iterable<Object> values);

    boolean exists(String id);

    boolean exists(Collection<String> ids);

    boolean exists(String field, Object value);

    long deleteAll(Iterable<String> keys);
}
