package com.github.romanqed.devspark.util;

import com.github.romanqed.devspark.gson.GsonObjectReader;
import com.github.romanqed.jfunc.Exceptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public final class Util {
    private static final ObjectReader READER = new GsonObjectReader();

    private Util() {
    }

    public static <T> T read(InputStream stream, Class<T> clazz) {
        return READER.read(stream, clazz);
    }

    public static <T> T read(File file, Class<T> clazz) {
        var stream = Exceptions.suppress(() -> new FileInputStream(file));
        var ret = READER.read(stream, clazz);
        Exceptions.suppress(stream::close);
        return ret;
    }
}
