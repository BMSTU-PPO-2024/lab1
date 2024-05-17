package com.github.romanqed.devspark.hash;

import com.github.romanqed.jfunc.Exceptions;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

public final class PBKDF2Encoder implements Encoder {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;
    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    private final byte[] salt;

    public PBKDF2Encoder() {
        this.salt = generateSalt();
    }

    private byte[] generateSalt() {
        var random = new SecureRandom();
        var ret = new byte[SALT_LENGTH];
        random.nextBytes(ret);
        return ret;
    }

    private String calculateHash(String value) {
        var spec = new PBEKeySpec(value.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        var factory = Exceptions.suppress(() -> SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"));
        var hash = Exceptions.suppress(() -> factory.generateSecret(spec).getEncoded());
        return ENCODER.encodeToString(hash);
    }

    @Override
    public String encode(String value) {
        Objects.requireNonNull(value);
        return calculateHash(value);
    }

    @Override
    public boolean matches(String raw, String encoded) {
        Objects.requireNonNull(raw);
        Objects.requireNonNull(encoded);
        return calculateHash(raw).equals(encoded);
    }
}
