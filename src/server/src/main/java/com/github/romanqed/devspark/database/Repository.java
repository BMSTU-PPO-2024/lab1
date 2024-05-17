package com.github.romanqed.devspark.database;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public interface Repository<V> {

    long put(V model);

    long put(Iterable<V> entities);

    long update(String key, V model);

    V get(String key);

    V get(String key, List<String> fields);

    boolean delete(String key);

    boolean delete(Iterable<String> keys);

    boolean delete(String field, Object value);

    boolean delete(String field, Iterable<Object> value);

    boolean delete(Map<String, Object> fields);

    Iterable<V> getAll();

    Iterable<V> getAll(Pagination pagination);

    Iterable<V> getAll(Iterable<String> keys);

    Iterable<V> getAll(Iterable<String> keys, Pagination pagination);

    V findFirstByField(String field, Object value);

    Iterable<V> findByField(String field, Object value);

    Iterable<V> findByField(String field, Object value, List<String> fields);

    Iterable<V> findByField(String field, Object value, Pagination pagination);

    Iterable<V> findByField(String field, Iterable<Object> values);

    Iterable<V> findByField(String field, Iterable<Object> values, Pagination pagination);

    Iterable<V> findByField(Map<String, Object> fields);

    Iterable<V> findByField(Map<String, Object> fields, Pagination pagination);

    Iterable<V> findMatched(String field, String pattern);

    Iterable<V> findMatched(String field, Pattern pattern);

    Iterable<V> findMatched(String field, String pattern, Pagination pagination);

    Iterable<V> findMatched(String field, Pattern pattern, Pagination pagination);

    Iterable<V> findMatchedWithFields(String field, String pattern, Map<String, Object> fields);

    Iterable<V> findMatchedWithFields(String field, Pattern pattern, Map<String, Object> fields);

    Iterable<V> findMatchedWithFields(String field,
                                      String pattern,
                                      Map<String, Object> fields,
                                      Pagination pagination);

    Iterable<V> findMatchedWithFields(String field,
                                      Pattern pattern,
                                      Map<String, Object> fields,
                                      Pagination pagination);

    long countByField(String field, Object value);

    long countByField(String field, Iterable<Object> values);

    long countByField(Map<String, Object> fields);

    boolean exists(String id);

    boolean exists(Collection<String> ids);

    boolean exists(String field, Object value);

    boolean exists(String field, Iterable<Object> value);

    boolean exists(Map<String, Object> fields);

    long deleteAll(Iterable<String> keys);

    long deleteAll(String field, Object value);

    long deleteAll(String field, Iterable<Object> value);

    long deleteAll(Map<String, Object> fields);
}
