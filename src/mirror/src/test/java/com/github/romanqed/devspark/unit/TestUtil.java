package com.github.romanqed.devspark.unit;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.Response;
import com.github.romanqed.devspark.jwt.JwtProvider;
import com.github.romanqed.devspark.mocks.ContextMock;
import com.github.romanqed.devspark.mocks.DecodedJWTMock;
import com.github.romanqed.devspark.mocks.JwtProviderMock;
import com.github.romanqed.devspark.mocks.RepositoryMock;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public final class TestUtil {
    private TestUtil() {
    }

    public static void testBadPathParams(Consumer<Context> consumer, Map<String, String> pathParams) {
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setPathParams(pathParams);
        consumer.accept(ctx);
        assertEquals(HttpStatus.BAD_REQUEST, ctx.status());
    }

    public static void testBadPagination(Consumer<Context> consumer) {
        testBadPathParams(consumer, Map.of("page", ""));
        testBadPathParams(consumer, Map.of("page", "-1"));
        testBadPathParams(consumer, Map.of("page", "0"));
        testBadPathParams(consumer, Map.of("batch", ""));
        testBadPathParams(consumer, Map.of("batch", "-1"));
        testBadPathParams(consumer, Map.of("batch", "0"));
    }

    public static void testBadPattern(Consumer<Context> consumer) {
        testBadPathParams(consumer, Map.of("pattern", "*"));
        testBadPathParams(consumer, Map.of("pattern", "?"));
        testBadPathParams(consumer, Map.of("pattern", "+"));
    }

    private static void testMatchingCase(Consumer<Context> consumer, String param, String value) {
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        if (param != null) {
            ctx.setQueryParams(Map.of(param, List.of(value)));
        } else {
            ctx.setQueryParams(Map.of());
        }
        consumer.accept(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertInstanceOf(List.class, ctx.getJson());
    }

    @SuppressWarnings("unchecked")
    public static void testMatching(Function<Repository<?>, Consumer<Context>> func) {
        var mock = new RepositoryMock<Object>() {
            boolean byName;
            boolean byPattern;
            boolean all;

            void reset() {
                byName = false;
                byPattern = false;
                all = false;
            }

            @Override
            public Iterable<Object> findByField(String field, Object value, Pagination pagination) {
                byName = true;
                return Collections.EMPTY_LIST;
            }

            @Override
            public Iterable<Object> findMatched(String field, Pattern pattern, Pagination pagination) {
                byPattern = true;
                return Collections.EMPTY_LIST;
            }

            @Override
            public Iterable<Object> getAll(Pagination pagination) {
                all = true;
                return Collections.EMPTY_LIST;
            }
        };
        mock.reset();
        var consumer = func.apply(mock);
        // By name
        testMatchingCase(consumer, "name", "");
        assertTrue(mock.byName);
        mock.reset();
        // By pattern
        testMatchingCase(consumer, "pattern", "");
        assertTrue(mock.byPattern);
        mock.reset();
        // All
        testMatchingCase(consumer, null, null);
        assertTrue(mock.all);
    }

    public static void testMissingToken(BiFunction<JwtProvider<?>, Repository<User>, Consumer<Context>> func,
                                        Supplier<ContextMock> supplier) {
        var consumer = func.apply(new JwtProviderMock<>() {
            @Override
            public Optional<DecodedJWT> validateToken(String token) {
                return Optional.empty();
            }
        }, null);
        var ctx = supplier.get();
        ctx.setHeaders(Map.of());
        consumer.accept(ctx);
        assertEquals(HttpStatus.UNAUTHORIZED, ctx.status());
    }

    public static void testMissingUser(BiFunction<JwtProvider<?>, Repository<User>, Consumer<Context>> func,
                                       Supplier<ContextMock> supplier) {
        var jwt = DecodedJWTMock.with(Map.of("id", "mock"));
        var provider = new JwtProviderMock<>();
        provider.setJwt(jwt);
        var users = new RepositoryMock<User>() {

            @Override
            public User get(String key) {
                assertEquals("mock", key);
                return null;
            }

            @Override
            public User get(String key, List<String> fields) {
                assertEquals("mock", key);
                return null;
            }
        };
        var consumer = func.apply(provider, users);
        var ctx = supplier.get();
        ctx.setHeaders(Map.of("Authorization", "Bearer token"));
        consumer.accept(ctx);
        assertEquals(HttpStatus.UNAUTHORIZED, ctx.status());
        assertEquals("User not found", ((Response) ctx.getJson()).getMessage());
    }

    public static void testBannedUser(BiFunction<JwtProvider<?>, Repository<User>, Consumer<Context>> func,
                                      Supplier<ContextMock> supplier) {
        var jwt = DecodedJWTMock.with(Map.of("id", "mock"));
        var provider = new JwtProviderMock<>();
        provider.setJwt(jwt);
        var users = new RepositoryMock<User>() {

            @Override
            public User get(String key) {
                assertEquals("mock", key);
                var ret = new User();
                ret.setBanned(true);
                return ret;
            }

            @Override
            public User get(String key, List<String> fields) {
                assertEquals("mock", key);
                var ret = new User();
                ret.setBanned(true);
                return ret;
            }
        };
        var consumer = func.apply(provider, users);
        var ctx = supplier.get();
        ctx.setHeaders(Map.of("Authorization", "Bearer token"));
        consumer.accept(ctx);
        assertEquals(HttpStatus.FORBIDDEN, ctx.status());
        assertEquals("User banned", ((Response) ctx.getJson()).getMessage());
    }

    public static void testMissingPermissions(BiFunction<JwtProvider<?>, Repository<User>, Consumer<Context>> func,
                                              Supplier<ContextMock> supplier) {
        var jwt = DecodedJWTMock.with(Map.of("id", "mock"));
        var provider = new JwtProviderMock<>();
        provider.setJwt(jwt);
        var users = new RepositoryMock<User>() {

            @Override
            public User get(String key) {
                assertEquals("mock", key);
                return new User();
            }

            @Override
            public User get(String key, List<String> fields) {
                assertEquals("mock", key);
                return new User();
            }
        };
        var consumer = func.apply(provider, users);
        var ctx = supplier.get();
        ctx.setHeaders(Map.of("Authorization", "Bearer token"));
        consumer.accept(ctx);
        assertEquals(HttpStatus.FORBIDDEN, ctx.status());
    }

    public static void testBadAuth(BiFunction<JwtProvider<?>, Repository<User>, Consumer<Context>> func,
                                   Supplier<ContextMock> supplier) {
        testMissingToken(func, supplier);
        testMissingUser(func, supplier);
        testBannedUser(func, supplier);
        testMissingPermissions(func, supplier);
    }
}
