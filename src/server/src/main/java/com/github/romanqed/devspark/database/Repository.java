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

    Iterable<V> findByField(String field, Object value, List<String> fields);

    Iterable<V> findByField(String field, Object value, Pagination pagination);

    Iterable<V> findAnd(Map<String, Object> fields, Pagination pagination);

    Iterable<V> findOr(Map<String, Object> fields1, Map<String, Object> fields2, Pagination pagination);

    Iterable<V> findMatched(String field, Pattern pattern, Pagination pagination);

    Iterable<V> findMatchedWithFields(String field,
                                      Pattern pattern,
                                      Map<String, Object> fields,
                                      Pagination pagination);

    Iterable<V> findMatchedWithFields(String field,
                                      Pattern pattern,
                                      Map<String, Object> fields1,
                                      Map<String, Object> fields2,
                                      Pagination pagination);

    boolean exists(String id);

    boolean exists(Collection<String> ids);

    boolean exists(Collection<String> ids, String field, Object value);

    boolean exists(String field, Object value);

    boolean exists(String field, Iterable<Object> value);

    boolean exists(Map<String, Object> fields);

    long deleteAll(Iterable<String> keys);

    long deleteAll(String field, Object value);
}
