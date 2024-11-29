package com.github.romanqed.devspark.jwt;

import com.auth0.jwt.algorithms.Algorithm;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public final class AuthConfig {
    private static final Map<String, Function<String, Algorithm>> ALGORITHMS = Map.of(
            "hmac256", Algorithm::HMAC256,
            "hmac384", Algorithm::HMAC384,
            "hmac512", Algorithm::HMAC512
    );
    private static final Map<TimeUnit, Integer> UNITS = Map.of(
            TimeUnit.MILLISECONDS, Calendar.MILLISECOND,
            TimeUnit.SECONDS, Calendar.SECOND,
            TimeUnit.MINUTES, Calendar.MINUTE,
            TimeUnit.HOURS, Calendar.HOUR_OF_DAY,
            TimeUnit.DAYS, Calendar.DAY_OF_YEAR
    );

    private String hmac;
    private String secret;
    private TimeUnit timeUnit;
    private int lifetime;

    public AuthConfig() {
        // Defaults
        this.hmac = "hmac256";
        this.timeUnit = TimeUnit.HOURS;
        this.lifetime = 1;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public Function<String, Algorithm> getHmacProvider() {
        var ret = ALGORITHMS.get(hmac.toLowerCase());
        if (ret == null) {
            throw new IllegalArgumentException("Invalid HMAC version: " + hmac);
        }
        return ret;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getCalendarUnit() {
        var ret = UNITS.get(timeUnit);
        if (ret == null) {
            throw new IllegalArgumentException("Unsupported time unit: " + timeUnit);
        }
        return ret;
    }

    public int getLifetime() {
        if (lifetime < 1) {
            throw new IllegalArgumentException("Lifetime must be greater than zero, current value: " + lifetime);
        }
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }
}
