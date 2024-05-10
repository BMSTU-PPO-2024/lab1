package com.github.romanqed.devspark.gson;

import com.google.gson.Gson;
import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public final class GsonMapper implements JsonMapper {
    private final Logger logger;
    private final Gson gson;

    public GsonMapper(Logger logger, Gson gson) {
        this.logger = logger;
        this.gson = gson;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromJsonStream(@NotNull InputStream json, @NotNull Type targetType) {
        var reader = new BufferedReader(new InputStreamReader(json, StandardCharsets.UTF_8));
        var ret = (T) gson.fromJson(reader, targetType);
        try {
            reader.close();
        } catch (IOException e) {
            logger.error("Cannot read object from json due to", e);
            throw new IllegalStateException("Cannot read object from json due to", e);
        }
        return ret;
    }

    @NotNull
    @Override
    public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
        return gson.fromJson(json, targetType);
    }

    @NotNull
    @Override
    public InputStream toJsonStream(@NotNull Object obj, @NotNull Type type) {
        var json = gson.toJson(obj, type);
        return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    }

    @NotNull
    @Override
    public String toJsonString(@NotNull Object obj, @NotNull Type type) {
        return gson.toJson(obj, type);
    }

    @Override
    public void writeToOutputStream(@NotNull Stream<?> stream, @NotNull OutputStream outputStream) {
        var writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        stream.forEach(e -> gson.toJson(e, writer));
        try {
            writer.close();
        } catch (IOException e) {
            logger.error("Cannot write json stream due to", e);
            throw new IllegalStateException("Cannot write json stream due to", e);
        }
    }
}
