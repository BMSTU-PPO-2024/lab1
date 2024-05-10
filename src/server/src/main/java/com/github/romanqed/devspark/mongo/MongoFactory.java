package com.github.romanqed.devspark.mongo;

import com.github.romanqed.devspark.database.Database;
import com.github.romanqed.devspark.database.DatabaseFactory;
import com.mongodb.client.MongoClient;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.jsr310.Jsr310CodecProvider;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

final class MongoFactory implements DatabaseFactory<ObjectId, Bson> {
    private final MongoClient client;

    MongoFactory(MongoClient client) {
        this.client = client;
    }

    @Override
    public Database<ObjectId, Bson> create(String name, Iterable<Class<?>> classes) {
        var builder = PojoCodecProvider.builder();
        classes.forEach(builder::register);
        var database = client
                .getDatabase(name)
                .withCodecRegistry(CodecRegistries.fromProviders(
                        builder.build(),
                        new Jsr310CodecProvider(),
                        new ValueCodecProvider()
                ));
        return new MongoDatabaseImpl(database);
    }
}