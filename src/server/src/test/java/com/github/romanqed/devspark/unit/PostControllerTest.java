package com.github.romanqed.devspark.unit;

import com.github.romanqed.devspark.controllers.PostController;
import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.PostDto;
import com.github.romanqed.devspark.dto.TextDto;
import com.github.romanqed.devspark.jwt.JwtProvider;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.mocks.ContextMock;
import com.github.romanqed.devspark.mocks.JwtProviderMock;
import com.github.romanqed.devspark.mocks.RepositoryMock;
import com.github.romanqed.devspark.models.Comment;
import com.github.romanqed.devspark.models.Post;
import com.github.romanqed.devspark.models.Tag;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public final class PostControllerTest {
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
        var post = new Post();
        post.setVisible(true);
        var posts = new RepositoryMock<Post>() {

            @Override
            public Post get(String key) {
                assertEquals("1", key);
                return post;
            }
        };
        var controller = new PostController(PROVIDER_MOCK, null, posts, null, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(Map.of());
        ctx.setPathParams(Map.of("postId", "1"));
        controller.get(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals(post, ctx.getJson());
    }

    // List comments
    @Test
    public void testListComments() {
        var posts = new RepositoryMock<Post>() {

            @Override
            public Post get(String key) {
                var ret = new Post();
                ret.setId(key);
                ret.setOwnerId("uid");
                return ret;
            }
        };
        var comments = new RepositoryMock<Comment>() {
            boolean retrieved = false;

            @Override
            public Iterable<Comment> findByField(String field, Object value, Pagination pagination) {
                assertEquals("postId", field);
                assertEquals("1", value);
                retrieved = true;
                return List.of();
            }
        };
        var controller = new PostController(PROVIDER_MOCK, USERS_MOCK, posts, comments, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("postId", "1"));
        ctx.setQueryParams(Map.of());
        controller.listComments(ctx);
        assertTrue(comments.retrieved);
        assertEquals(HttpStatus.OK, ctx.status());
        assertInstanceOf(List.class, ctx.getJson());
    }

    // Publish comment
    @Test
    public void testPublishComment() {
        var posts = new RepositoryMock<Post>() {

            @Override
            public Post get(String key) {
                var ret = new Post();
                ret.setId(key);
                ret.setOwnerId("uid");
                return ret;
            }
        };
        var comments = new RepositoryMock<Comment>() {
            Comment comment;

            @Override
            public boolean put(Comment model) {
                assertEquals("1", model.getPostId());
                assertEquals("text", model.getText());
                assertEquals("uid", model.getOwnerId());
                comment = model;
                return true;
            }
        };
        var dto = new TextDto();
        dto.setText("text");
        var controller = new PostController(PROVIDER_MOCK, USERS_MOCK, posts, comments, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setBody(dto);
        ctx.setPathParams(Map.of("postId", "1"));
        controller.publishComment(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals(comments.comment, ctx.getJson());
    }

    // Update
    @Test
    public void testUpdate() {
        var posts = new RepositoryMock<Post>() {
            boolean updated = false;

            @Override
            public Post get(String key) {
                var ret = new Post();
                ret.setId(key);
                ret.setOwnerId("uid");
                return ret;
            }

            @Override
            public boolean update(String key, Post model) {
                updated = true;
                assertEquals("1", key);
                assertEquals("title", model.getTitle());
                assertEquals("text", model.getText());
                assertFalse(model.isVisible());
                assertEquals(Set.of("t1"), model.getTagIds());
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
        dto.setVisible(false);
        dto.setTagIds(Set.of("t1"));
        var controller = new PostController(PROVIDER_MOCK, USERS_MOCK, posts, null, tags);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setBody(dto);
        ctx.setPathParams(Map.of("postId", "1"));
        controller.update(ctx);
        assertTrue(posts.updated);
        assertEquals(HttpStatus.OK, ctx.status());
    }

    // Delete
    @Test
    public void testDelete() {
        var posts = new RepositoryMock<Post>() {
            boolean deleted = false;

            @Override
            public boolean delete(Map<String, Object> fields) {
                deleted = true;
                return true;
            }
        };
        var comments = new RepositoryMock<Comment>() {
            boolean deleted = false;

            @Override
            public long deleteAll(String field, Object value) {
                deleted = true;
                return 0;
            }
        };
        var controller = new PostController(PROVIDER_MOCK, USERS_MOCK, posts, comments, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("postId", "1"));
        controller.delete(ctx);
        assertTrue(posts.deleted);
        assertTrue(comments.deleted);
        assertEquals(HttpStatus.OK, ctx.status());
    }

    // Rate
    @Test
    public void testRate() {
        var post = new Post();
        post.setOwnerId("uid");
        var scores = new HashMap<String, Integer>();
        post.setScores(scores);
        var posts = new RepositoryMock<Post>() {
            @Override
            public Post get(String key) {
                return post;
            }
        };
        var controller = new PostController(PROVIDER_MOCK, USERS_MOCK, posts, null, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("postId", ""));
        controller.addRate(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals(1, scores.get("uid"));
    }

    // Unrate
    @Test
    public void testUnrate() {
        var post = new Post();
        post.setOwnerId("uid");
        var scores = new HashMap<String, Integer>();
        scores.put("uid", 1);
        post.setScores(scores);
        var posts = new RepositoryMock<Post>() {
            @Override
            public Post get(String key) {
                return post;
            }
        };
        var controller = new PostController(PROVIDER_MOCK, USERS_MOCK, posts, null, null);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("postId", ""));
        controller.deleteRate(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertNull(scores.get("uid"));
    }
}
