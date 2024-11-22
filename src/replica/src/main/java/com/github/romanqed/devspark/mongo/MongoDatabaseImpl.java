package com.github.romanqed.devspark.mongo;

import com.github.romanqed.devspark.database.Database;
import com.github.romanqed.devspark.database.Repository;
import com.mongodb.client.MongoDatabase;

final class MongoDatabaseImpl implements Database {

    private final MongoDatabase database;

    MongoDatabaseImpl(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public <V> Repository<V> create(String name, Class<V> type) {
        var collection = database.getCollection(name, type);
        return new MongoRepository<>(collection);
    }
}
