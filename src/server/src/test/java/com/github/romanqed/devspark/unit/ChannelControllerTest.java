package com.github.romanqed.devspark.unit;

import com.github.romanqed.devspark.controllers.ChannelController;
import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.ChannelDto;
import com.github.romanqed.devspark.dto.PostDto;
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

public final class ChannelControllerTest {
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

    // Get
    @Test
    public void testGet() {
        var channel = new Channel();
        channel.setVisible(true);
        var channels = new RepositoryMock<Channel>() {
            @Override
            public Channel get(String key) {
                assertEquals("1", key);
                return channel;
            }
        };
        var controller = new ChannelController(PROVIDER_MOCK, null, channels, null, null, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(Map.of());
        ctx.setPathParams(Map.of("channelId", "1"));
        controller.get(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals(channel, ctx.getJson());
    }

    // Find
    @Test
    public void testFind() {
        var channels = new RepositoryMock<Channel>() {
            boolean retrieved = false;

            @Override
            public Iterable<Channel> findByField(String field, Object value, Pagination pagination) {
                retrieved = true;
                assertEquals("visible", field);
                assertEquals(true, value);
                return List.of();
            }
        };
        var controller = new ChannelController(PROVIDER_MOCK, null, channels, null, null, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(Map.of());
        ctx.setQueryParams(Map.of());
        controller.find(ctx);
        assertTrue(channels.retrieved);
        assertEquals(HttpStatus.OK, ctx.status());
        assertInstanceOf(List.class, ctx.getJson());
    }

    // List posts
    @Test
    public void testListPosts() {
        var channels = new RepositoryMock<Channel>() {
            @Override
            public Channel get(String key) {
                var ret = new Channel();
                ret.setId(key);
                ret.setVisible(true);
                return ret;
            }
        };
        var posts = new RepositoryMock<Post>() {
            boolean retrieved = false;

            @Override
            public Iterable<Post> findByField(String field, Object value, Pagination pagination) {
                retrieved = true;
                assertEquals("channelId", field);
                assertEquals("1", value);
                return List.of();
            }
        };
        var controller = new ChannelController(PROVIDER_MOCK, null, channels, posts, null, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(Map.of());
        ctx.setPathParams(Map.of("channelId", "1"));
        ctx.setQueryParams(Map.of());
        controller.listPosts(ctx);
        assertTrue(posts.retrieved);
        assertEquals(HttpStatus.OK, ctx.status());
        assertInstanceOf(List.class, ctx.getJson());
    }

    // Put
    @Test
    public void testPut() {
        var channels = new RepositoryMock<Channel>() {
            Channel channel;

            @Override
            public boolean put(Channel model) {
                assertEquals("uid", model.getOwnerId());
                assertEquals("channel", model.getName());
                assertTrue(model.isVisible());
                channel = model;
                return true;
            }
        };
        var dto = new ChannelDto();
        dto.setName("channel");
        dto.setVisible(true);
        var controller = new ChannelController(PROVIDER_MOCK, USERS_MOCK, channels, null, null, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setBody(dto);
        controller.put(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals(channels.channel, ctx.getJson());
    }

    // Publish post
    @Test
    public void testPublishPost() {
        var channels = new RepositoryMock<Channel>() {

            @Override
            public Channel get(String key) {
                var ret = new Channel();
                ret.setId(key);
                ret.setOwnerId("uid");
                return ret;
            }
        };
        var posts = new RepositoryMock<Post>() {
            Post post;

            @Override
            public boolean put(Post model) {
                assertEquals("uid", model.getOwnerId());
                assertEquals("1", model.getChannelId());
                assertEquals("title", model.getTitle());
                assertEquals("text", model.getText());
                assertEquals(Set.of("t1"), model.getTagIds());
                assertTrue(model.isVisible());
                post = model;
                return true;
            }
        };
        var tags = new RepositoryMock<Tag>() {

            @Override
            public boolean exists(Collection<String> ids) {
                return true;
            }
        };
        var dto = new PostDto();
        dto.setTitle("title");
        dto.setText("text");
        dto.setTagIds(Set.of("t1"));
        dto.setVisible(true);
        var controller = new ChannelController(PROVIDER_MOCK, USERS_MOCK, channels, posts, null, tags);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setPathParams(Map.of("channelId", "1"));
        ctx.setHeaders(HEADERS);
        ctx.setBody(dto);
        controller.publishPost(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals(posts.post, ctx.getJson());
    }

    // Update
    @Test
    public void testUpdate() {
        var channels = new RepositoryMock<Channel>() {
            boolean updated = false;

            @Override
            public Channel get(String key) {
                var ret = new Channel();
                ret.setId(key);
                ret.setOwnerId("uid");
                return ret;
            }

            @Override
            public boolean update(String key, Channel model) {
                assertEquals("1", key);
                assertEquals("name", model.getName());
                assertTrue(model.isVisible());
                updated = true;
                return true;
            }
        };
        var dto = new ChannelDto();
        dto.setName("name");
        dto.setVisible(true);
        var controller = new ChannelController(PROVIDER_MOCK, USERS_MOCK, channels, null, null, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setPathParams(Map.of("channelId", "1"));
        ctx.setHeaders(HEADERS);
        ctx.setBody(dto);
        controller.update(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertTrue(channels.updated);
    }

    // Delete
    @Test
    public void testDelete() {
        var channels = new RepositoryMock<Channel>() {
            boolean deleted;

            @Override
            public boolean delete(Map<String, Object> fields) {
                assertEquals(Map.of("_id", "1", "ownerId", "uid"), fields);
                deleted = true;
                return true;
            }
        };
        var posts = new RepositoryMock<Post>() {
            boolean deleted;

            @Override
            public Iterable<Post> findByField(String field, Object value, List<String> fields) {
                assertEquals("channelId", field);
                assertEquals("1", value);
                return List.of();
            }

            @Override
            public long deleteAll(Iterable<String> keys) {
                deleted = true;
                return 0;
            }
        };
        var comments = new RepositoryMock<Comment>() {
            boolean deleted = false;

            @Override
            public boolean deleteAll(String field, Collection<?> values) {
                assertEquals("postId", field);
                deleted = true;
                return true;
            }
        };
        var controller = new ChannelController(PROVIDER_MOCK, USERS_MOCK, channels, posts, comments, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setPathParams(Map.of("channelId", "1"));
        ctx.setHeaders(HEADERS);
        controller.delete(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertTrue(channels.deleted);
        assertTrue(posts.deleted);
        assertTrue(comments.deleted);
    }
}
