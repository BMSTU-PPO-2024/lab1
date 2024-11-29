package com.github.romanqed.devspark.gson;

import com.github.romanqed.devspark.util.ObjectReader;
import com.github.romanqed.jfunc.Exceptions;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class GsonObjectReader implements ObjectReader {
    private static final Gson GSON = new Gson();

    @Override
    public <T> T read(InputStream stream, Class<T> clazz) {
        var reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        var ret = (T) GSON.fromJson(reader, clazz);
        Exceptions.suppress(reader::close);
        return ret;
    }
}
