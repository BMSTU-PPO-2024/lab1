package com.github.romanqed.devspark.mocks;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ClaimMock implements Claim {
    private Object value;

    public ClaimMock(Object value) {
        this.value = value;
    }

    public static Claim of(Object value) {
        return new ClaimMock(value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean isNull() {
        return value == null;
    }

    @Override
    public boolean isMissing() {
        return value == null;
    }

    @Override
    public Boolean asBoolean() {
        return (Boolean) value;
    }

    @Override
    public Integer asInt() {
        return (Integer) value;
    }

    @Override
    public Long asLong() {
        return (Long) value;
    }

    @Override
    public Double asDouble() {
        return (Double) value;
    }

    @Override
    public String asString() {
        return (String) value;
    }

    @Override
    public Date asDate() {
        return (Date) value;
    }

    @Override
    public <T> T[] asArray(Class<T> aClass) throws JWTDecodeException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<T> asList(Class<T> aClass) throws JWTDecodeException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> asMap() throws JWTDecodeException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T as(Class<T> aClass) throws JWTDecodeException {
        try {
            return aClass.cast(value);
        } catch (ClassCastException e) {
            throw new JWTDecodeException("Cast failed", e);
        }
    }
}
