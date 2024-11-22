package com.github.romanqed.devspark.commands;

import com.github.romanqed.devspark.javalin.CliCommand;
import com.github.romanqed.devspark.javalin.TextCommand;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import org.bson.BsonDocument;
import org.bson.BsonInt64;

@CliCommand("ping-db")
public final class PingDatabaseCommand implements TextCommand {
    private final MongoClient client;

    public PingDatabaseCommand(MongoClient client) {
        this.client = client;
    }

    @Override
    public void execute(String[] arguments) {
        var admin = client.getDatabase("admin");
        // Ping db
        try {
            // Send a ping to confirm a successful connection
            var command = new BsonDocument("ping", new BsonInt64(1));
            admin.runCommand(command);
            System.out.println("MongoDB client successfully connected to database");
        } catch (MongoException e) {
            System.out.println("Cannot connect to MongoDB due to " + e.getMessage());
            e.printStackTrace();
        }
    }
}
