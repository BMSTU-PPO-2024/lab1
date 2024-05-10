package com.github.romanqed.devspark.mongo;

import com.github.romanqed.devspark.database.Database;
import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.di.ProviderConsumer;
import com.github.romanqed.devspark.di.ServiceProviderConsumer;
import com.github.romanqed.devspark.util.Util;
import com.github.romanqed.jtype.Types;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.amayaframework.di.ServiceProvider;
import io.github.amayaframework.di.ServiceProviderBuilder;
import org.atteo.classindex.ClassIndex;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.File;
import java.lang.reflect.Type;

@ProviderConsumer
public final class MongoServiceConsumer implements ServiceProviderConsumer {
    private static final File MONGO_CONFIG = new File("mongo.json");
    private static final Type DATABASE_TYPE = Types.of(Database.class, ObjectId.class, Bson.class);

    @Override
    public void pre(ServiceProviderBuilder builder) {
        var config = Util.read(MONGO_CONFIG, MongoConfig.class);
        var client = MongoClients.create(config.getUrl());
        var factory = new MongoFactory(client);
        // Find models
        var models = ClassIndex.getAnnotated(Model.class);
        var database = factory.create(config.getDatabase(), models);
        // Add adapter for each model
        for (var clazz : models) {
            var model = clazz.getAnnotation(Model.class);
            var repository = database.create(model.value(), clazz);
            var type = Types.of(Repository.class, ObjectId.class, clazz, Bson.class);
            builder.addService(type, () -> repository);
        }
        builder.addService(DATABASE_TYPE, () -> database);
        builder.addService(MongoClient.class, () -> client);
    }

    @Override
    public void post(ServiceProvider provider) {
    }
}
