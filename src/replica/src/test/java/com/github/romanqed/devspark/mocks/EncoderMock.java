package com.github.romanqed.devspark.mocks;

import com.github.romanqed.devspark.hash.Encoder;

public class EncoderMock implements Encoder {

    @Override
    public String encode(String value) {
        return value;
    }

    @Override
    public boolean matches(String raw, String encoded) {
        return raw.equals(encoded);
    }
}
