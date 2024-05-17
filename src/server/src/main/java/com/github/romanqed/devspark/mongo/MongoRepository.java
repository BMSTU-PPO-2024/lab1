package com.github.romanqed.devspark.mongo;

import com.github.romanqed.devspark.database.Repository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import java.util.Collection;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

final class MongoRepository<V> implements Repository<V> {
    private final MongoCollection<V> collection;

    MongoRepository(MongoCollection<V> collection) {
        this.collection = collection;
    }

    @Override
    public long put(V model) {
        return collection.insertOne(model).wasAcknowledged() ? 1 : 0;
    }

    @Override
    public long update(String key, V model) {
        return collection.replaceOne(eq("_id", key), model).getModifiedCount();
    }

    @Override
    public long put(Iterable<V> entities) {
        var ret = 0L;
        for (var entity : entities) {
            ret += put(entity);
        }
        return ret;
    }

    @Override
    public V get(String key) {
        return collection.find(eq(key)).first();
    }

    @Override
    public V findFirstByField(String field, Object value) {
        return collection.find(Filters.eq(field, value)).first();
    }

    @Override
    public Iterable<V> findByField(String field, Object value) {
        return collection.find(Filters.eq(field, value));
    }

    @Override
    public Iterable<V> findByField(String field, Iterable<Object> values) {
        return collection.find(Filters.in(field, values));
    }

    @Override
    public Iterable<V> findMatched(String field, String pattern) {
        return collection.find(Filters.regex(field, pattern));
    }

    @Override
    public Iterable<V> getAll() {
        return collection.find();
    }

    @Override
    public Iterable<V> getAll(Iterable<String> keys) {
        return collection.find(Filters.in("_id", keys));
    }

    @Override
    public long countByField(String field, Object value) {
        return collection.countDocuments(Filters.eq(field, value));
    }

    @Override
    public long countByField(String field, Iterable<Object> values) {
        return collection.countDocuments(Filters.in(field, values));
    }

    @Override
    public boolean exists(String id) {
        return collection.countDocuments(Filters.eq("_id", id)) == 1;
    }

    @Override
    public boolean exists(Collection<String> ids) {
        return collection.countDocuments(Filters.in("_id", ids)) == ids.size();
    }

    @Override
    public boolean exists(String field, Object value) {
        return collection.countDocuments(Filters.eq(field, value)) == 1;
    }

    @Override
    public long delete(String key) {
        return collection.deleteOne(eq(key)).getDeletedCount();
    }

    @Override
    public long deleteAll(Iterable<String> keys) {
        return collection.deleteMany(Filters.in("_id", keys)).getDeletedCount();
    }

    // Batched
    @Override
    public Iterable<V> getAll(int page, int batch) {
        return collection.find().skip(batch * (page - 1)).limit(batch);
    }

    @Override
    public Iterable<V> getAll(Iterable<String> keys, int page, int batch) {
        return collection
                .find(Filters.in("_id", keys))
                .skip(batch * (page - 1))
                .limit(batch);
    }

    @Override
    public Iterable<V> findByField(String field, Object value, int page, int batch) {
        return collection
                .find(Filters.eq(field, value))
                .skip(batch * (page - 1))
                .limit(batch);
    }

    @Override
    public Iterable<V> findByField(String field, Iterable<Object> values, int page, int batch) {
        return collection
                .find(Filters.in(field, values))
                .skip(batch * (page - 1))
                .limit(batch);
    }

    @Override
    public Iterable<V> findMatched(String field, String pattern, int page, int batch) {
        return collection
                .find(Filters.regex(field, pattern))
                .skip(batch * (page - 1))
                .limit(batch);
    }

    public Iterable<V> findMatched(String field, Pattern pattern, int page, int batch) {
        return collection
                .find(Filters.regex(field, pattern))
                .skip(batch * (page - 1))
                .limit(batch);
    }
}
