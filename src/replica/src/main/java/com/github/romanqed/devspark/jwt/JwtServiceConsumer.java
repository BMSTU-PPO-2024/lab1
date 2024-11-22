package com.github.romanqed.devspark.jwt;

import com.auth0.jwt.JWT;
import com.github.romanqed.devspark.di.ProviderConsumer;
import com.github.romanqed.devspark.di.ServiceProviderConsumer;
import com.github.romanqed.devspark.util.Util;
import com.github.romanqed.jtype.Types;
import io.github.amayaframework.di.ServiceProvider;
import io.github.amayaframework.di.ServiceProviderBuilder;
import javalinjwt.JWTGenerator;
import javalinjwt.JWTProvider;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

@ProviderConsumer
public final class JwtServiceConsumer implements ServiceProviderConsumer {
    private static final File AUTH_CONFIG = new File(System.getenv("AUTH_CONFIG"));
    private static final Type JWT_PROVIDER_TYPE = Types.of(JwtProvider.class, JwtUser.class);

    private static Date getExpirationTime(int unit, int lifetime) {
        var calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(unit, lifetime);
        return calendar.getTime();
    }

    @Override
    public void pre(ServiceProviderBuilder builder) {
        var config = Util.read(AUTH_CONFIG, AuthConfig.class);
        var secret = config.getSecret();
        var hmac = config.getHmacProvider();
        var unit = config.getCalendarUnit();
        var lifetime = config.getLifetime();
        var generator = (JWTGenerator<JwtUser>) (user, algorithm) -> {
            var token = JWT.create()
                    .withClaim("id", user.getId())
                    .withClaim("email", user.getEmail())
                    .withClaim("nickname", user.getNickname())
                    .withExpiresAt(getExpirationTime(unit, lifetime));
            return token.sign(algorithm);
        };
        var algorithm = hmac.apply(secret);
        var verifier = JWT.require(algorithm).build();
        var jwtProvider = new JWTProvider<>(algorithm, generator, verifier);
        var wrapper = new JwtProviderImpl<>(jwtProvider);
        builder.addService(JWT_PROVIDER_TYPE, () -> wrapper);
    }

    @Override
    public void post(ServiceProvider provider) {
    }
}
