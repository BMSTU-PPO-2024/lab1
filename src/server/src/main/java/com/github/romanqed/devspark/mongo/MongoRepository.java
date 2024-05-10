package com.github.romanqed.devspark.mongo;

import com.github.romanqed.devspark.database.Repository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

final class MongoRepository<V> implements Repository<ObjectId, V, Bson> {
    private final MongoCollection<V> collection;

    MongoRepository(MongoCollection<V> collection) {
        this.collection = collection;
    }

    @Override
    public long put(V model) {
        return collection.insertOne(model).wasAcknowledged() ? 1 : 0;
    }

    @Override
    public long update(ObjectId key, V model) {
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
    public V find(ObjectId key) {
        return findFirst(eq(key));
    }

    @Override
    public Iterable<V> findAll(Iterable<ObjectId> keys) {
        return collection.find(Filters.in("_id", keys));
    }

    @Override
    public Iterable<V> filter(Bson filter) {
        return collection.find(filter);
    }

    @Override
    public Iterable<V> filterAndSort(Bson filter, Bson sort) {
        return collection.find(filter).sort(sort);
    }

    @Override
    public V findFirst(Bson filter) {
        return collection.find(filter).first();
    }

    @Override
    public long count(Bson filter) {
        return collection.countDocuments(filter);
    }

    @Override
    public long deleteFirst(Bson filter) {
        return collection.deleteOne(filter).getDeletedCount();
    }

    @Override
    public long deleteAll(Bson filter) {
        return collection.deleteMany(filter).getDeletedCount();
    }

    @Override
    public long delete(ObjectId key) {
        return deleteFirst(eq(key));
    }

    @Override
    public long deleteAll(Iterable<ObjectId> keys) {
        return deleteAll(Filters.in("_id", keys));
    }
}
