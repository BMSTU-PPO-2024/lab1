package com.github.romanqed.devspark.mongo;

import com.mongodb.client.MongoClients;

public final class MongoDatabaseTest {
    private static final String CONNECTION_STRING = "mongodb://127.0.0.1:27017";

    public static void main(String[] args) {
        var client = MongoClients.create(CONNECTION_STRING);
        var factory = new MongoFactory(client);
    }
}
