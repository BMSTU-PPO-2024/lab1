package com.github.romanqed.devspark;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.Tag;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import javalinjwt.JWTProvider;
import org.bson.types.ObjectId;

@JavalinController
public final class Test {
    private final Repository<Tag> rp;

    public Test(JWTProvider<JwtUser> provider, Repository<Tag> rp) {
        this.rp = rp;
    }

    @Route(method = HandlerType.GET, route = "/test")
    public void generate(Context ctx) {
        var tag = new Tag();
        tag.setId(new ObjectId().toString());
        tag.setName("TestTag");
        rp.put(tag);
    }

    @Route(method = HandlerType.GET, route = "/validate")
    public void validate(Context ctx) {

    }
}

class JWTResponse {
    String token;

    public JWTResponse(String token) {
        this.token = token;
    }
}
