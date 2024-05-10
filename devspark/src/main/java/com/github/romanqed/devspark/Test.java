package com.github.romanqed.devspark;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

@JavalinController("/hello")
public final class Test {
    private final Repository<ObjectId, MyModel, Bson> myms;

    public Test(Logger logger, Repository<ObjectId, MyModel, Bson> repository) {
        logger.info("Hello from Test ctl");
        this.myms = repository;
    }

    @Route(method = HandlerType.GET)
    public void get(Context ctx) {
        ctx.status(HttpStatus.OK);
        ctx.json(new MyDTO());
        myms.put(new MyModel());
    }

    static final class MyDTO {
        String helloNigga = "Nill Kiggers";
    }

    @Model("myrepo")
    public static final class MyModel {
        public String myString = "Hello, bitches";
    }
}
