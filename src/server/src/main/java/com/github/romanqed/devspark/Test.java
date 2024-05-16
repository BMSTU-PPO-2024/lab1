package com.github.romanqed.devspark;

import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtUser;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import javalinjwt.JWTProvider;
import javalinjwt.JavalinJWT;

@JavalinController
public final class Test {
    private final JWTProvider<JwtUser> provider;

    public Test(JWTProvider<JwtUser> provider) {
        this.provider = provider;
    }

    @Route(method = HandlerType.GET, route = "/generate")
    public void generate(Context ctx) {
        // a mock user as an examples
        var user = new JwtUser("0", "admin", "admin@admin.com");
        // generate a token for the user
        var token = provider.generateToken(user);
        // send the JWT response
        ctx.json(new JWTResponse(token));
    }

    @Route(method = HandlerType.GET, route = "/validate")
    public void validate(Context ctx) {
        var decodedJWT = JavalinJWT
                .getTokenFromHeader(ctx)
                .flatMap(provider::validateToken);

        if (decodedJWT.isEmpty()) {
            ctx.status(401).result("Missing or invalid token");
            return;
        }
        var jwt = decodedJWT.get();
        var user = JwtUser.fromJWT(jwt);
        ctx.result(
                "Hi " + user.getNickname() + "! Your token expires at " + jwt.getExpiresAt()
        );
    }
}

class JWTResponse {
    String token;

    public JWTResponse(String token) {
        this.token = token;
    }
}
