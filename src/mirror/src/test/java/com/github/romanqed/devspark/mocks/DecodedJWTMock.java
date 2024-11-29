package com.github.romanqed.devspark.mocks;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecodedJWTMock implements DecodedJWT {
    private Map<String, Claim> claims;

    public static DecodedJWTMock with(Map<String, Object> claims) {
        var ret = new DecodedJWTMock();
        var map = new HashMap<String, Claim>();
        claims.forEach((k, v) -> map.put(k, ClaimMock.of(v)));
        ret.setClaims(map);
        return ret;
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public String getHeader() {
        return null;
    }

    @Override
    public String getPayload() {
        return null;
    }

    @Override
    public String getSignature() {
        return null;
    }

    @Override
    public String getAlgorithm() {
        return "Mock";
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public String getKeyId() {
        return null;
    }

    @Override
    public Claim getHeaderClaim(String s) {
        return null;
    }

    @Override
    public String getIssuer() {
        return null;
    }

    @Override
    public String getSubject() {
        return null;
    }

    @Override
    public List<String> getAudience() {
        return null;
    }

    @Override
    public Date getExpiresAt() {
        return new Date(Integer.MAX_VALUE);
    }

    @Override
    public Date getNotBefore() {
        return new Date(Integer.MIN_VALUE);
    }

    @Override
    public Date getIssuedAt() {
        return new Date(Integer.MAX_VALUE);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Claim getClaim(String s) {
        return claims.get(s);
    }

    @Override
    public Map<String, Claim> getClaims() {
        return claims;
    }

    public void setClaims(Map<String, Claim> claims) {
        this.claims = claims;
    }
}
