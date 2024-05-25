package com.github.romanqed.devspark.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class MongoDatabaseTest {
    private static final String CONNECTION_STRING = "mongodb://127.0.0.1:27017";
    private static final String DATABASE = "test";
    private static final String COLLECTION = "entities";
    private static final String ID = "testId";

    public static void main(String[] args) {
        var client = MongoClients.create(CONNECTION_STRING);
        try {
            test(client);
        } finally {
            client.getDatabase(DATABASE).drop();
            client.close();
        }
    }

    private static void test(MongoClient client) {
        var factory = new MongoFactory(client);
        var database = factory.create(DATABASE, List.of(TestEntity.class));
        var repository = database.create(COLLECTION, TestEntity.class);
        var entity = create();
        // Create
        requireTrue(repository.put(entity));
        // Read
        requireTrue(repository.exists(ID));
        requireTrue(match(entity, repository.get(ID)));
        // Update
        entity.i = 10;
        requireTrue(repository.update(ID, entity));
        requireTrue(repository.get(ID).i == 10);
        // Delete
        requireTrue(repository.delete(ID));
        requireFalse(repository.exists(ID));
        requireTrue(repository.get(ID) == null);
    }

    private static void requireTrue(boolean value) {
        if (!value) {
            throw new IllegalStateException("Check failed");
        }
    }

    private static void requireFalse(boolean value) {
        if (value) {
            throw new IllegalStateException("Check failed");
        }
    }

    private static TestEntity create() {
        var ret = new TestEntity();
        ret.id = ID;
        ret.i = 5;
        ret.d = 0.03;
        ret.c = 'r';
        ret.l = List.of("1", "2", "3");
        ret.s = Set.of("4", "5", "6");
        return ret;
    }

    private static boolean match(TestEntity e1, TestEntity e2) {
        return Objects.equals(e1.id, e2.id)
                && e1.i == e2.i
                && Double.compare(e1.d, e2.d) == 0
                && e1.c == e2.c
                && Objects.equals(e1.l, e2.l)
                && Objects.equals(e1.s, e2.s);
    }
}
