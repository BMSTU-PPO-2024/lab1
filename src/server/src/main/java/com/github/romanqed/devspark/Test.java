package com.github.romanqed.devspark;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.models.Topic;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import java.util.Set;

@JavalinController("/hello")
public final class Test {
    private final Repository<ObjectId, Topic, Bson> topics;

    public Test(Logger logger, Repository<ObjectId, Topic, Bson> topics) {
        logger.info("Hello from Test ctl");
        this.topics = topics;
    }

    @Route(method = HandlerType.GET)
    public void get(Context ctx) {
        ctx.status(HttpStatus.OK);
        var topic = new Topic();
        topic.setName("TestTopic");
        topic.setTagIds(Set.of(new ObjectId()));
        topics.put(topic);
    }
}
