package com.github.romanqed.devspark.unit;

import com.github.romanqed.devspark.controllers.FeedController;
import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.FeedDto;
import com.github.romanqed.devspark.jwt.JwtProvider;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.mocks.ContextMock;
import com.github.romanqed.devspark.mocks.JwtProviderMock;
import com.github.romanqed.devspark.mocks.RepositoryMock;
import com.github.romanqed.devspark.models.*;
import io.javalin.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public final class FeedControllerTest {
    private static final JwtProvider<JwtUser> PROVIDER_MOCK = JwtProviderMock.withJwt(Map.of("id", "mock"));
    private static final Map<String, String> HEADERS = Map.of("Authorization", "Bearer token");
    private static final Repository<User> USERS_MOCK = new RepositoryMock<>() {

        @Override
        public User get(String key, List<String> fields) {
            var ret = new User();
            ret.setId("uid");
            return ret;
        }
    };
    private static final Repository<Channel> CHANNELS_MOCK = new RepositoryMock<>() {
        @Override
        public boolean exists(Collection<String> ids, String field, Object value) {
            return true;
        }
    };
    private static final Repository<Tag> TAGS_MOCK = new RepositoryMock<>() {
        @Override
        public boolean exists(Collection<String> ids) {
            return true;
        }
    };

    // Get
    @Test
    public void testGet() {
        var feed = new Feed();
        feed.setOwnerId("uid");
        var feeds = new RepositoryMock<Feed>() {

            @Override
            public Feed get(String key) {
                assertEquals("1", key);
                return feed;
            }
        };
        var controller = new FeedController(PROVIDER_MOCK, USERS_MOCK, feeds, null, null, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("feedId", "1"));
        controller.get(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals(feed, ctx.getJson());
    }

    // Find
    @Test
    public void testFind() {
        var feeds = new RepositoryMock<Feed>() {
            boolean retrieved = false;

            @Override
            public Iterable<Feed> findByField(String field, Object value, Pagination pagination) {
                retrieved = true;
                assertEquals("visible", field);
                assertEquals(true, value);
                return List.of();
            }
        };
        var controller = new FeedController(PROVIDER_MOCK, null, feeds, null, null, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(Map.of());
        ctx.setQueryParams(Map.of());
        controller.find(ctx);
        assertTrue(feeds.retrieved);
        assertEquals(HttpStatus.OK, ctx.status());
        assertInstanceOf(List.class, ctx.getJson());
    }

    // List posts
    @Test
    public void testListPosts() {
        var feeds = new RepositoryMock<Feed>() {
            @Override
            public Feed get(String key) {
                var ret = new Feed();
                ret.setOwnerId("uid");
                ret.setTagIds(Set.of());
                ret.setChannelIds(Set.of());
                return ret;
            }
        };
        var posts = new RepositoryMock<Post>() {
            boolean retrieved = false;

            @Override
            public Iterable<Post> findByField(String field, Object value, Pagination pagination) {
                assertEquals("visible", field);
                assertEquals(true, value);
                retrieved = true;
                return List.of();
            }
        };
        var controller = new FeedController(PROVIDER_MOCK, USERS_MOCK, feeds, posts, null, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("feedId", "1"));
        ctx.setQueryParams(Map.of());
        controller.listPosts(ctx);
        assertTrue(posts.retrieved);
        assertEquals(HttpStatus.OK, ctx.status());
        assertInstanceOf(List.class, ctx.getJson());
    }

    // Put
    @Test
    public void testPut() {
        var dto = new FeedDto();
        dto.setName("feed");
        dto.setVisible(true);
        dto.setChannelIds(Set.of("c1"));
        dto.setTagIds(Set.of("t1"));
        var feeds = new RepositoryMock<Feed>() {
            @Override
            public boolean put(Feed model) {
                assertEquals("uid", model.getOwnerId());
                assertEquals("feed", model.getName());
                assertTrue(model.isVisible());
                assertEquals(Set.of("c1"), model.getChannelIds());
                assertEquals(Set.of("t1"), model.getTagIds());
                return true;
            }
        };
        var controller = new FeedController(PROVIDER_MOCK, USERS_MOCK, feeds, null, CHANNELS_MOCK, TAGS_MOCK);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setBody(dto);
        ctx.setQueryParams(Map.of());
        controller.put(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertInstanceOf(Feed.class, ctx.getJson());
    }

    // Update
    @Test
    public void testUpdate() {
        var dto = new FeedDto();
        dto.setName("feed");
        dto.setVisible(true);
        dto.setChannelIds(Set.of("c1"));
        dto.setTagIds(Set.of("t1"));
        var feeds = new RepositoryMock<Feed>() {
            boolean updated = false;

            @Override
            public Feed get(String key) {
                var ret = new Feed();
                ret.setId(key);
                ret.setOwnerId("uid");
                return ret;
            }

            @Override
            public boolean update(String key, Feed model) {
                assertEquals("1", key);
                assertEquals("feed", model.getName());
                assertTrue(model.isVisible());
                assertEquals(Set.of("c1"), model.getChannelIds());
                assertEquals(Set.of("t1"), model.getTagIds());
                updated = true;
                return true;
            }
        };
        var controller = new FeedController(PROVIDER_MOCK, USERS_MOCK, feeds, null, CHANNELS_MOCK, TAGS_MOCK);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setBody(dto);
        ctx.setQueryParams(Map.of());
        ctx.setPathParams(Map.of("feedId", "1"));
        controller.update(ctx);
        assertTrue(feeds.updated);
        assertEquals(HttpStatus.OK, ctx.status());
    }

    @Test
    public void testDelete() {
        var feeds = new RepositoryMock<Feed>() {
            boolean deleted = false;

            @Override
            public boolean delete(Map<String, Object> fields) {
                deleted = true;
                return true;
            }
        };
        var controller = new FeedController(PROVIDER_MOCK, USERS_MOCK, feeds, null, null, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setQueryParams(Map.of());
        ctx.setPathParams(Map.of("feedId", "1"));
        controller.delete(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertTrue(feeds.deleted);
    }
}
