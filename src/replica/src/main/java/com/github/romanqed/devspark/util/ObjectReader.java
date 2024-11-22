package com.github.romanqed.devspark.util;

import java.io.InputStream;

public interface ObjectReader {
    <T> T read(InputStream stream, Class<T> clazz);
}
