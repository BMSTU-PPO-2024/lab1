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
        try {
            // Create
            requireTrue("Put", repository.put(entity));
            // Read
            requireTrue("Exists", repository.exists(ID));
            requireTrue("Get", match(entity, repository.get(ID)));
            // Update
            entity.i = 10;
            requireTrue("Update", repository.update(ID, entity));
            requireTrue("Get updated", repository.get(ID).i == 10);
            // Delete
            requireTrue("Delete", repository.delete(ID));
            requireFalse("Exists deleted", repository.exists(ID));
            requireTrue("Get deleted is null", repository.get(ID) == null);
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void requireTrue(String name, boolean value) {
        if (!value) {
            throw new IllegalStateException("Test '" + name + "' failed");
        }
        System.out.println("Test '" + name + "' passed");
    }

    private static void requireFalse(String name, boolean value) {
        if (value) {
            throw new IllegalStateException("Test '" + name + "' failed");
        }
        System.out.println("Test '" + name + "' passed");
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
