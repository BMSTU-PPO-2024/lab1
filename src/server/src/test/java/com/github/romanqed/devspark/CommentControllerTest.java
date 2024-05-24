package com.github.romanqed.devspark;

import com.github.romanqed.devspark.controllers.CommentController;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.TextDto;
import com.github.romanqed.devspark.jwt.JwtProvider;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.mocks.ContextMock;
import com.github.romanqed.devspark.mocks.JwtProviderMock;
import com.github.romanqed.devspark.mocks.RepositoryMock;
import com.github.romanqed.devspark.models.Comment;
import com.github.romanqed.devspark.models.Permissions;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.github.romanqed.devspark.mocks.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

public final class CommentControllerTest {
    private static final Repository<User> USERS_MOCK = new RepositoryMock<>() {
        @Override
        public User get(String key, List<String> fields) {
            var ret = new User();
            ret.setId("uid");
            return ret;
        }
    };
    private static final Repository<User> ADMINS_MOCK = new RepositoryMock<>() {
        @Override
        public User get(String key, List<String> fields) {
            var ret = new User();
            ret.setId("aid");
            ret.setPermissions(Permissions.MANAGE_COMMENTS.getValue());
            return ret;
        }
    };
    private static final JwtProvider<JwtUser> PROVIDER_MOCK = JwtProviderMock.withJwt(Map.of("id", "uid"));
    private static final Map<String, String> HEADERS = Map.of("Authorization", "Bearer token");

    // Get
    @Test
    public void testGetNotFound() {
        var controller = new CommentController(null, null, new RepositoryMock<>());
        var ctx = new ContextMock();
        ctx.setPathParams(Map.of("commentId", ""));
        controller.get(ctx);
        assertEquals(HttpStatus.NOT_FOUND, ctx.status());
    }

    @Test
    public void testGet() {
        var comments = new RepositoryMock<Comment>() {
            final Comment comment = new Comment();

            @Override
            public Comment get(String key) {
                return comment;
            }
        };
        var controller = new CommentController(null, null, comments);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setPathParams(Map.of("commentId", ""));
        controller.get(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals(comments.comment, ctx.getJson());
    }

    // Update
    @Test
    public void testUpdateMissingBody() {
        var controller = new CommentController(null, null, null);
        var ctx = new ContextMock();
        controller.update(ctx);
        assertEquals(HttpStatus.BAD_REQUEST, ctx.status());
    }

    @Test
    public void testUpdateMissingText() {
        var controller = new CommentController(null, null, null);
        var ctx = new ContextMock();
        var dto = new TextDto();
        dto.setText(null);
        ctx.setBody(dto);
        controller.update(ctx);
        assertEquals(HttpStatus.BAD_REQUEST, ctx.status());
    }

    @Test
    public void testUpdateNotFound() {
        var controller = new CommentController(PROVIDER_MOCK, USERS_MOCK, new RepositoryMock<>());
        var ctx = new ContextMock();
        var dto = new TextDto();
        dto.setText("t");
        ctx.setHeaders(HEADERS);
        ctx.setBody(dto);
        ctx.setPathParams(Map.of("commentId", ""));
        controller.update(ctx);
        assertEquals(HttpStatus.NOT_FOUND, ctx.status());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUpdateBadAuth() {
        var supplier = (Supplier<ContextMock>) () -> {
            var ret = new ContextMock();
            var dto = new TextDto();
            ret.setPathParams(Map.of("commentId", ""));
            dto.setText("");
            ret.setBody(dto);
            return ret;
        };
        var comment = new Comment();
        comment.setOwnerId("");
        var comments = new RepositoryMock<Comment>() {

            @Override
            public Comment get(String key) {
                return comment;
            }
        };
        var func = (BiFunction<JwtProvider<?>, Repository<User>, Consumer<Context>>) (p, us) ->
                new CommentController((JwtProvider<JwtUser>) p, us, comments)::update;
        testBadAuth(func, supplier);
    }

    @Test
    public void testUpdate() {
        var comments = new RepositoryMock<Comment>() {
            Comment comment;

            @Override
            public Comment get(String key) {
                var ret = new Comment();
                ret.setOwnerId("uid");
                return ret;
            }

            @Override
            public long update(String key, Comment model) {
                comment = model;
                return 1;
            }
        };
        var controller = new CommentController(PROVIDER_MOCK, USERS_MOCK, comments);
        var ctx = new ContextMock();
        var dto = new TextDto();
        dto.setText("t");
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setBody(dto);
        ctx.setPathParams(Map.of("commentId", ""));
        controller.update(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals("t", comments.comment.getText());
    }

    // Delete
    @Test
    @SuppressWarnings("unchecked")
    public void testDeleteBadAuth() {
        var supplier = (Supplier<ContextMock>) () -> {
            var ret = new ContextMock();
            ret.setPathParams(Map.of("commentId", ""));
            return ret;
        };
        var func = (BiFunction<JwtProvider<?>, Repository<User>, Consumer<Context>>) (p, us) ->
                new CommentController((JwtProvider<JwtUser>) p, us, null)::delete;
        testMissingToken(func, supplier);
        testMissingUser(func, supplier);
        testBannedUser(func, supplier);
    }

    @Test
    public void testDeleteNotFound() {
        var controller = new CommentController(PROVIDER_MOCK, USERS_MOCK, new RepositoryMock<>());
        var ctx = new ContextMock();
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("commentId", ""));
        controller.delete(ctx);
        assertEquals(HttpStatus.FORBIDDEN, ctx.status());
    }

    @Test
    public void testAdminDeleteNotFound() {
        var controller = new CommentController(PROVIDER_MOCK, ADMINS_MOCK, new RepositoryMock<>());
        var ctx = new ContextMock();
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("commentId", ""));
        controller.delete(ctx);
        assertEquals(HttpStatus.NOT_FOUND, ctx.status());
    }

    @Test
    public void testAdminDelete() {
        var comments = new RepositoryMock<Comment>() {
            boolean deleted = false;

            @Override
            public boolean delete(String key) {
                deleted = true;
                return true;
            }
        };
        var controller = new CommentController(PROVIDER_MOCK, ADMINS_MOCK, comments);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("commentId", ""));
        controller.delete(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertTrue(comments.deleted);
    }

    @Test
    public void testDelete() {
        var comments = new RepositoryMock<Comment>() {
            boolean deleted = false;

            @Override
            public boolean delete(Map<String, Object> fields) {
                deleted = true;
                return true;
            }
        };
        var controller = new CommentController(PROVIDER_MOCK, USERS_MOCK, comments);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("commentId", ""));
        controller.delete(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertTrue(comments.deleted);
    }

    // Rate
    @Test
    public void testRate() {
        var comment = new Comment();
        var scores = new HashMap<String, Integer>();
        comment.setScores(scores);
        var comments = new RepositoryMock<Comment>() {
            @Override
            public Comment get(String key) {
                return comment;
            }
        };
        var controller = new CommentController(PROVIDER_MOCK, USERS_MOCK, comments);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("commentId", ""));
        controller.addRate(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals(1, scores.get("uid"));
    }

    // Unrate
    @Test
    public void testUnrate() {
        var comment = new Comment();
        var scores = new HashMap<String, Integer>();
        scores.put("uid", 1);
        comment.setScores(scores);
        var comments = new RepositoryMock<Comment>() {

            @Override
            public Comment get(String key) {
                return comment;
            }
        };
        var controller = new CommentController(PROVIDER_MOCK, USERS_MOCK, comments);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("commentId", ""));
        controller.deleteRate(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertNull(scores.get("uid"));
    }
}
