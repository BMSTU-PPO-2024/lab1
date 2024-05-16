package com.github.romanqed.devspark.mongo;

import com.github.romanqed.devspark.database.Repository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.Collection;

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
    public V find(String key) {
        return findFirst(eq(key));
    }

    @Override
    public Iterable<V> findAll(Iterable<String> keys) {
        return collection.find(Filters.in("_id", keys));
    }

    @Override
    public Iterable<V> filter(Object filter) {
        return collection.find((Bson) filter);
    }

    @Override
    public Iterable<V> filterAndSort(Object filter, Object sort) {
        return collection.find((Bson) filter).sort((Bson) sort);
    }

    @Override
    public V findFirst(Object filter) {
        return collection.find((Bson) filter).first();
    }

    @Override
    public long count(Object filter) {
        return collection.countDocuments((Bson) filter);
    }

    @Override
    public boolean exists(String id) {
        return count(Filters.eq("_id", id)) == 1;
    }

    @Override
    public boolean exists(Collection<String> ids) {
        return count(Filters.in("_id", ids)) == ids.size();
    }

    @Override
    public long deleteFirst(Object filter) {
        return collection.deleteOne((Bson) filter).getDeletedCount();
    }

    @Override
    public long deleteAll(Object filter) {
        return collection.deleteMany((Bson) filter).getDeletedCount();
    }

    @Override
    public long delete(String key) {
        return deleteFirst(eq(key));
    }

    @Override
    public long deleteAll(Iterable<String> keys) {
        return deleteAll(Filters.in("_id", keys));
    }
}
