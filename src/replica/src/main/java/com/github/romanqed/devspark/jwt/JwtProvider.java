package com.github.romanqed.devspark.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Optional;

public interface JwtProvider<T> {

    String generateToken(T obj);

    Optional<DecodedJWT> validateToken(String token);
}
