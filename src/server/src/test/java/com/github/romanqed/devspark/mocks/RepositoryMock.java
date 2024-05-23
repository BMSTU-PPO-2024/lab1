package com.github.romanqed.devspark.mocks;

import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RepositoryMock<V> implements Repository<V> {

    @Override
    public long put(V model) {
        return 0;
    }

    @Override
    public long put(Iterable<V> entities) {
        return 0;
    }

    @Override
    public long update(String key, V model) {
        return 0;
    }

    @Override
    public V get(String key) {
        return null;
    }

    @Override
    public V get(String key, List<String> fields) {
        return null;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }

    @Override
    public boolean delete(Iterable<String> keys) {
        return false;
    }

    @Override
    public boolean delete(String field, Object value) {
        return false;
    }

    @Override
    public boolean delete(String field, Iterable<Object> value) {
        return false;
    }

    @Override
    public boolean delete(Map<String, Object> fields) {
        return false;
    }

    @Override
    public Iterable<V> getAll() {
        return null;
    }

    @Override
    public Iterable<V> getAll(Pagination pagination) {
        return null;
    }

    @Override
    public Iterable<V> getAll(Iterable<String> keys) {
        return null;
    }

    @Override
    public Iterable<V> getAll(Iterable<String> keys, Pagination pagination) {
        return null;
    }

    @Override
    public V findFirstByField(String field, Object value) {
        return null;
    }

    @Override
    public Iterable<V> findByField(String field, Object value, List<String> fields) {
        return null;
    }

    @Override
    public Iterable<V> findByField(String field, Object value, Pagination pagination) {
        return null;
    }

    @Override
    public Iterable<V> findByField(Map<String, Iterable<Object>> in, Map<String, Object> eq, Pagination pagination) {
        return null;
    }

    @Override
    public Iterable<V> findAnd(Map<String, Object> fields, Pagination pagination) {
        return null;
    }

    @Override
    public Iterable<V> findOr(Map<String, Object> fields1, Map<String, Object> fields2, Pagination pagination) {
        return null;
    }

    @Override
    public Iterable<V> findMatched(String field, Pattern pattern, Pagination pagination) {
        return null;
    }

    @Override
    public Iterable<V> findMatchedWithFields(String field, Pattern pattern, Map<String, Object> fields, Pagination pagination) {
        return null;
    }

    @Override
    public Iterable<V> findMatchedWithFields(String field, Pattern pattern, Map<String, Object> fields1, Map<String, Object> fields2, Pagination pagination) {
        return null;
    }

    @Override
    public boolean exists(String id) {
        return false;
    }

    @Override
    public boolean exists(Collection<String> ids) {
        return false;
    }

    @Override
    public boolean exists(Collection<String> ids, String field, Object value) {
        return false;
    }

    @Override
    public boolean exists(String field, Object value) {
        return false;
    }

    @Override
    public boolean exists(String field, Iterable<Object> value) {
        return false;
    }

    @Override
    public boolean exists(Map<String, Object> fields) {
        return false;
    }

    @Override
    public long deleteAll(Iterable<String> keys) {
        return 0;
    }

    @Override
    public long deleteAll(String field, Object value) {
        return 0;
    }
}
