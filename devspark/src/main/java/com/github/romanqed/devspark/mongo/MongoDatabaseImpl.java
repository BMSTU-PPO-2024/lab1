package com.github.romanqed.devspark.mongo;

import com.github.romanqed.devspark.database.Database;
import com.github.romanqed.devspark.database.Repository;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

final class MongoDatabaseImpl implements Database<ObjectId, Bson> {

    private final MongoDatabase database;

    MongoDatabaseImpl(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public <V> Repository<ObjectId, V, Bson> create(String name, Class<V> type) {
        var collection = database.getCollection(name, type);
        return new MongoRepository<>(collection);
    }
}
