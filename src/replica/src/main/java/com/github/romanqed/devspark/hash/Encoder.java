package com.github.romanqed.devspark.hash;

public interface Encoder {
    String encode(String value);

    boolean matches(String raw, String encoded);
}
