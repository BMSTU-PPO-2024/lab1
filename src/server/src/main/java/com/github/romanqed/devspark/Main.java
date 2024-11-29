package com.github.romanqed.devspark;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.di.ScanProviderDirector;
import com.github.romanqed.devspark.hash.Encoder;
import com.github.romanqed.devspark.javalin.CliManager;
import com.github.romanqed.devspark.javalin.ServerConfig;
import com.github.romanqed.devspark.models.User;
import com.github.romanqed.jtype.Types;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import io.github.amayaframework.di.*;
import io.javalin.Javalin;
import org.apache.log4j.PropertyConfigurator;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {
    private static final String LOGGER_CONFIG = "log4j.properties";

    public static void main(String[] args) {
        // Configure logger
        PropertyConfigurator.configure(LOGGER_CONFIG);
        // Configure DI
        var repository = new HashRepository();
        var builder = Builders.createChecked();
        builder.setRepository(repository);
        // Add project logger
        var logger = LoggerFactory.getLogger("devspark");
        builder.addService(Logger.class, () -> logger);
        // Process all di-dependent actions
        var director = new ScanProviderDirector();
        director.setBuilder(builder);
        var provider = (ServiceProvider) null;
        // Check DI errors
        try {
            provider = director.build();
        } catch (TypeNotFoundException e) {
            logger.error("DI cannot find type {}", e.getType());
            throw e;
        } catch (CycleFoundException e) {
            logger.error("DI found cycle: {}", e.getCycle());
            throw e;
        }
        // Start Mongo instance
        var mongo = startMongo(provider, logger);
        // Init admin user
        initAdminUser(provider, logger);
        // Start Javalin instance
        var javalin = startJavalin(provider, logger);
        // Bind stop actions by exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            javalin.stop();
            mongo.close();
        }));
        // Start cli
        var manager = provider.instantiate(CliManager.class);
        if (manager != null) {
            manager.handle(System.in);
        }
    }

    private static void attachOpenApi(Javalin javalin) {
        var config = javalin.unsafeConfig();
        config.staticFiles.add("/swagger");
    }

    private static Javalin startJavalin(ServiceProvider provider, Logger logger) {
        var javalin = provider.instantiate(Javalin.class);
        attachOpenApi(javalin);
        var config = provider.instantiate(ServerConfig.class);
        var host = config.getHost();
        var port = config.getPort();
        if (host == null || host.isEmpty()) {
            javalin.start(port);
            logger.info("Javalin instance successfully started on localhost:{}", port);
        } else {
            javalin.start(host, port);
            logger.info("Javalin instance successfully started on {}:{}", host, port);
        }
        return javalin;
    }

    private static MongoClient startMongo(ServiceProvider provider, Logger logger) {
        var client = provider.instantiate(MongoClient.class);
        var admin = client.getDatabase("admin");
        // Ping db
        try {
            // Send a ping to confirm a successful connection
            var command = new BsonDocument("ping", new BsonInt64(1));
            admin.runCommand(command);
            logger.info("MongoDB client successfully connected to database");
        } catch (MongoException e) {
            logger.error("Cannot connect to MongoDB due to", e);
            throw new IllegalStateException("Cannot connect to MongoDB due to", e);
        }
        return client;
    }

    @SuppressWarnings("unchecked")
    private static void initAdminUser(ServiceProvider provider, Logger logger) {
        var config = provider.instantiate(ServerConfig.class);
        var encoder = provider.instantiate(Encoder.class);
        var users = (Repository<User>) provider.instantiate(Types.of(Repository.class, User.class));
        if (users.exists("email", config.getLogin())) {
            logger.info("Admin user exists");
            return;
        }
        var login = config.getLogin();
        var user = User.of(login, encoder.encode(config.getPassword()));
        user.setNickname(login);
        user.setPermissions(Integer.MAX_VALUE);
        users.put(user);
        logger.info("Admin user successfully created");
    }
}
