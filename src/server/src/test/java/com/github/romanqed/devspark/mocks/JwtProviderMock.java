package com.github.romanqed.devspark.mocks;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.romanqed.devspark.jwt.JwtProvider;

import java.util.Map;
import java.util.Optional;

public class JwtProviderMock<T> implements JwtProvider<T> {
    private T tokenObj;
    private DecodedJWT jwt;

    public static <T> JwtProviderMock<T> withJwt(Map<String, Object> claims) {
        var ret = new JwtProviderMock<T>();
        ret.setJwt(DecodedJWTMock.with(claims));
        return ret;
    }

    public T getTokenObj() {
        return tokenObj;
    }

    public void setTokenObj(T tokenObj) {
        this.tokenObj = tokenObj;
    }

    public DecodedJWT getJwt() {
        return jwt;
    }

    public void setJwt(DecodedJWT jwt) {
        this.jwt = jwt;
    }

    @Override
    public String generateToken(T obj) {
        this.tokenObj = obj;
        return "token";
    }

    @Override
    public Optional<DecodedJWT> validateToken(String token) {
        return Optional.of(jwt);
    }
}
