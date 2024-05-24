package com.github.romanqed.devspark.mocks;

import io.javalin.config.Key;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import io.javalin.json.JsonMapper;
import io.javalin.plugin.ContextPlugin;
import io.javalin.security.RouteRole;
import io.javalin.validation.Validator;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ContextMock implements Context {
    private HttpStatus status;
    private HandlerType type;
    private Object body;
    private String endpointHandlerPath;
    private String matchedPath;
    private Map<String, String> headers;
    private Map<String, String> pathParams;
    private Map<String, List<String>> queryParams;
    private Object json;
    private Type jsonType;

    public HandlerType getType() {
        return type;
    }

    public void setType(HandlerType type) {
        this.type = type;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getEndpointHandlerPath() {
        return endpointHandlerPath;
    }

    public void setEndpointHandlerPath(String endpointHandlerPath) {
        this.endpointHandlerPath = endpointHandlerPath;
    }

    public String getMatchedPath() {
        return matchedPath;
    }

    public void setMatchedPath(String matchedPath) {
        this.matchedPath = matchedPath;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setPathParams(Map<String, String> pathParams) {
        this.pathParams = pathParams;
    }

    public void setQueryParams(Map<String, List<String>> queryParams) {
        this.queryParams = queryParams;
    }

    public Object getJson() {
        return json;
    }

    public Type getJsonType() {
        return jsonType;
    }

    @Override
    public <T> T appData(@NotNull Key<T> key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T bodyAsClass(@NotNull Class<T> clazz) {
        return clazz.cast(body);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T bodyAsClass(@NotNull Type type) {
        return bodyAsClass((Class<? extends T>) type);
    }

    @NotNull
    @Override
    public String endpointHandlerPath() {
        return endpointHandlerPath;
    }

    @Override
    public void future(@NotNull Supplier<? extends CompletableFuture<?>> supplier) {
        // Skip futures
    }

    @NotNull
    @Override
    public HandlerType handlerType() {
        return type;
    }

    @Nullable
    @Override
    public String header(@NotNull String header) {
        return headers.get(header);
    }

    @NotNull
    @Override
    public <T> Validator<T> headerAsClass(@NotNull String header, @NotNull Class<T> clazz) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Map<String, String> headerMap() {
        return headers;
    }

    @NotNull
    @Override
    public Context json(@NotNull Object obj) {
        return json(obj, obj.getClass());
    }

    @NotNull
    @Override
    public Context json(@NotNull Object obj, @NotNull Type type) {
        this.json = obj;
        this.jsonType = type;
        return this;
    }

    @NotNull
    @Override
    public JsonMapper jsonMapper() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public String matchedPath() {
        return matchedPath;
    }

    @NotNull
    @Override
    public Context minSizeForCompression(int i) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public ServletOutputStream outputStream() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public String pathParam(@NotNull String s) {
        return pathParams.get(s);
    }

    @NotNull
    @Override
    public <T> Validator<T> pathParamAsClass(@NotNull String key, @NotNull Class<T> clazz) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Map<String, String> pathParamMap() {
        return pathParams;
    }

    @Nullable
    @Override
    public String queryParam(@NotNull String key) {
        var ret = queryParams.get(key);
        if (ret == null || ret.isEmpty()) {
            return null;
        }
        return ret.get(0);
    }

    @NotNull
    @Override
    public <T> Validator<T> queryParamAsClass(@NotNull String key, @NotNull Class<T> clazz) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Map<String, List<String>> queryParamMap() {
        return queryParams;
    }

    @NotNull
    @Override
    public List<String> queryParams(@NotNull String key) {
        return queryParams.get(key);
    }

    @Override
    public void redirect(@NotNull String s, @NotNull HttpStatus httpStatus) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public HttpServletRequest req() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public HttpServletResponse res() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Context result(@NotNull InputStream inputStream) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public InputStream resultInputStream() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Set<RouteRole> routeRoles() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Context skipRemainingHandlers() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public HttpStatus status() {
        return status;
    }

    @NotNull
    @Override
    public Context status(@NotNull HttpStatus status) {
        this.status = status;
        return this;
    }

    @NotNull
    @Override
    public Context status(int status) {
        this.status = HttpStatus.forStatus(status);
        return this;
    }

    @Override
    public <T> T with(@NotNull Class<? extends ContextPlugin<?, T>> aClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeJsonStream(@NotNull Stream<?> stream) {
        throw new UnsupportedOperationException();
    }
}
