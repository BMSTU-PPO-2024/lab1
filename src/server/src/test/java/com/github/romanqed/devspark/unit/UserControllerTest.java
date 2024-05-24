package com.github.romanqed.devspark.unit;

import com.github.romanqed.devspark.controllers.UserController;
import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.UserDto;
import com.github.romanqed.devspark.hash.Encoder;
import com.github.romanqed.devspark.jwt.JwtProvider;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.mocks.ContextMock;
import com.github.romanqed.devspark.mocks.EncoderMock;
import com.github.romanqed.devspark.mocks.JwtProviderMock;
import com.github.romanqed.devspark.mocks.RepositoryMock;
import com.github.romanqed.devspark.models.Channel;
import com.github.romanqed.devspark.models.Feed;
import com.github.romanqed.devspark.models.Permissions;
import com.github.romanqed.devspark.models.User;
import com.github.romanqed.jfunc.Exceptions;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

public final class UserControllerTest {
    private static final Repository<User> USERS_MOCK = new RepositoryMock<>() {

        @Override
        public User get(String key) {
            return new User();
        }

        @Override
        public User get(String key, List<String> fields) {
            return new User();
        }
    };
    private static final JwtProvider<JwtUser> PROVIDER_MOCK = JwtProviderMock.withJwt(Map.of("id", "mock"));
    private static final Map<String, String> HEADERS = Map.of("Authorization", "Bearer token");
    private static final Encoder ENCODER_MOCK = new EncoderMock();

    private void testGet(BiConsumer<UserController, Context> consumer, Map<String, String> pathParams) {
        var controller = new UserController(
                PROVIDER_MOCK,
                USERS_MOCK,
                null,
                null,
                null
        );
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(pathParams);
        consumer.accept(controller, ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertInstanceOf(UserDto.class, ctx.getJson());
    }

    // Get
    @Test
    public void testGetSelf() {
        testGet(UserController::getSelf, Map.of());
    }

    @Test
    public void testGet() {
        testGet(UserController::get, Map.of("userId", ""));
    }

    private void testUpdate(Repository<User> users,
                            BiConsumer<UserController, Context> consumer,
                            Map<String, String> pathParams) {
        var controller = new UserController(
                PROVIDER_MOCK,
                users,
                null,
                null,
                ENCODER_MOCK
        );
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(pathParams);
        var dto = new UserDto();
        dto.setNickname("nick");
        dto.setPassword("psd");
        dto.setAbout("abt");
        dto.setAvatar(Exceptions.suppress(() -> new URL("https://ya.ru")));
        ctx.setBody(dto);
        consumer.accept(controller, ctx);
        assertEquals(HttpStatus.OK, ctx.status());
    }

    // Update
    @Test
    public void testUpdateSelf() {
        var users = new RepositoryMock<User>() {

            @Override
            public User get(String key) {
                return new User();
            }

            @Override
            public long update(String key, User model) {
                assertEquals("nick", model.getNickname());
                assertEquals("psd", model.getPassword());
                assertEquals("abt", model.getAbout());
                assertEquals("https://ya.ru", model.getAvatar().toString());
                return 1;
            }
        };
        testUpdate(users, UserController::updateSelf, Map.of());
    }

    @Test
    public void testUpdate() {
        var users = new RepositoryMock<User>() {

            @Override
            public User get(String key) {
                assertEquals("uid", key);
                var ret = new User();
                ret.setId(key);
                return ret;
            }

            @Override
            public User get(String key, List<String> fields) {
                assertEquals("mock", key);
                var ret = new User();
                ret.setPermissions(Permissions.MANAGE_USERS.getValue());
                return ret;
            }

            @Override
            public long update(String key, User model) {
                assertEquals("uid", key);
                assertEquals("nick", model.getNickname());
                assertEquals("psd", model.getPassword());
                assertEquals("abt", model.getAbout());
                assertEquals("https://ya.ru", model.getAvatar().toString());
                return 1;
            }
        };
        testUpdate(users, UserController::update, Map.of("userId", "uid"));
    }

    @SuppressWarnings("unchecked")
    private void testList(BiConsumer<UserController, Context> consumer, Map<String, String> pathParams) {
        var repository = new RepositoryMock<>() {
            boolean found;

            @Override
            public Iterable<Object> findAnd(Map<String, Object> fields, Pagination pagination) {
                found = true;
                return List.of();
            }
        };
        var controller = new UserController(
                PROVIDER_MOCK,
                USERS_MOCK,
                (Repository<Channel>) (Repository<?>) repository,
                (Repository<Feed>) (Repository<?>) repository,
                null
        );
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setQueryParams(Map.of());
        ctx.setPathParams(pathParams);
        consumer.accept(controller, ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertTrue(repository.found);
    }

    // List channels
    @Test
    public void testListSelfChannels() {
        testList(UserController::listSelfChannels, Map.of());
    }

    @Test
    public void testListChannels() {
        testList(UserController::listChannels, Map.of("userId", "uid"));
    }

    // List feeds
    @Test
    public void testListSelfFeeds() {
        testList(UserController::listSelfFeeds, Map.of());
    }

    @Test
    public void testListFeeds() {
        testList(UserController::listFeeds, Map.of("userId", "uid"));
    }
}
