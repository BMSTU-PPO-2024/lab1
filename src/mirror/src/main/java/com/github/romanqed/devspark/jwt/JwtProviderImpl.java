package com.github.romanqed.devspark.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import javalinjwt.JWTProvider;

import java.util.Optional;

final class JwtProviderImpl<T> implements JwtProvider<T> {
    private final JWTProvider<T> provider;

    JwtProviderImpl(JWTProvider<T> provider) {
        this.provider = provider;
    }

    @Override
    public String generateToken(T obj) {
        return provider.generateToken(obj);
    }

    @Override
    public Optional<DecodedJWT> validateToken(String token) {
        return provider.validateToken(token);
    }
}
