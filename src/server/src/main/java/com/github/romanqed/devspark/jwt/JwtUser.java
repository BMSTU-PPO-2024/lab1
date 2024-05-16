package com.github.romanqed.devspark.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;

public final class JwtUser {
    private String id;
    private String nickname;
    private String email;

    public JwtUser(String id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }

    public static JwtUser fromJWT(DecodedJWT jwt) {
        var id = jwt.getClaim("id").asString();
        var nickname = jwt.getClaim("nickname").asString();
        var email = jwt.getClaim("email").asString();
        return new JwtUser(id, nickname, email);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
