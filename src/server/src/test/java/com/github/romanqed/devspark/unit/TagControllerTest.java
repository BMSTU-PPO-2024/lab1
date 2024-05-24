package com.github.romanqed.devspark.unit;

import com.github.romanqed.devspark.controllers.TagController;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.Response;
import com.github.romanqed.devspark.dto.TagDto;
import com.github.romanqed.devspark.jwt.JwtProvider;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.mocks.ContextMock;
import com.github.romanqed.devspark.mocks.JwtProviderMock;
import com.github.romanqed.devspark.mocks.RepositoryMock;
import com.github.romanqed.devspark.mocks.TestUtil;
import com.github.romanqed.devspark.models.Permissions;
import com.github.romanqed.devspark.models.Tag;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.romanqed.devspark.mocks.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TagControllerTest {
    private static final Repository<User> USERS_MOCK = new RepositoryMock<>() {
        @Override
        public User get(String key, List<String> fields) {
            var ret = new User();
            ret.setPermissions(Permissions.MANAGE_TAGS.getValue());
            return ret;
        }
    };
    private static final JwtProvider<JwtUser> PROVIDER_MOCK = JwtProviderMock.withJwt(Map.of("id", "mock"));
    private static final Map<String, String> HEADERS = Map.of("Authorization", "Bearer token");

    // Get
    @Test
    public void testGetNotFound() {
        var tags = new RepositoryMock<Tag>();
        var controller = new TagController(null, null, tags);
        var ctx = new ContextMock();
        ctx.setPathParams(Map.of("tagId", "id"));
        controller.get(ctx);
        assertEquals(HttpStatus.NOT_FOUND, ctx.status());
    }

    @Test
    public void testGet() {
        var tag = new Tag();
        var tags = new RepositoryMock<Tag>() {
            @Override
            public Tag get(String key) {
                return tag;
            }
        };
        var controller = new TagController(null, null, tags);
        var ctx = new ContextMock();
        ctx.setPathParams(Map.of("tagId", "id"));
        controller.get(ctx);
        assertEquals(tag, ctx.getJson());
    }

    // Find
    @Test
    public void testInvalidPagination() {
        var controller = new TagController(null, null, null);
        testBadPagination(controller::find);
    }

    @Test
    public void testInvalidPattern() {
        var controller = new TagController(null, null, null);
        testBadPattern(controller::find);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFind() {
        TestUtil.testMatching(r -> new TagController(null, null, (Repository<Tag>) r)::find);
    }

    private void testInvalidBody(TagController controller, Function<TagController, Consumer<Context>> func) {
        var consumer = func.apply(controller);
        var nullCtx = new ContextMock();
        var badCtx = new ContextMock();
        var dto = new TagDto();
        badCtx.setBody(dto);
        consumer.accept(nullCtx);
        assertEquals(HttpStatus.BAD_REQUEST, nullCtx.status());
        assertEquals("Missing body", ((Response) nullCtx.getJson()).getMessage());
        consumer.accept(badCtx);
        assertEquals(HttpStatus.BAD_REQUEST, badCtx.status());
        assertEquals("Tag name is null", ((Response) badCtx.getJson()).getMessage());
    }

    // Put
    @Test
    public void testPutInvalidBody() {
        var controller = new TagController(null, null, null);
        testInvalidBody(controller, c -> c::put);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPutBadAuth() {
        var supplier = (Supplier<ContextMock>) () -> {
            var ctx = new ContextMock();
            var dto = new TagDto();
            dto.setName("");
            ctx.setBody(dto);
            return ctx;
        };
        testBadAuth((p, us) -> new TagController((JwtProvider<JwtUser>) p, us, null)::put, supplier);
    }

    @Test
    public void testExistingTag() {
        var tags = new RepositoryMock<Tag>() {

            @Override
            public boolean exists(String field, Object value) {
                return true;
            }
        };
        var controller = new TagController(PROVIDER_MOCK, USERS_MOCK, tags);
        var ctx = new ContextMock();
        ctx.setHeaders(HEADERS);
        var dto = new TagDto();
        dto.setName("");
        ctx.setBody(dto);
        controller.put(ctx);
        assertEquals(HttpStatus.CONFLICT, ctx.status());
    }

    @Test
    public void testPut() {
        var tags = new RepositoryMock<Tag>() {
            Tag tag;

            @Override
            public long put(Tag model) {
                tag = model;
                return 1;
            }
        };
        var controller = new TagController(PROVIDER_MOCK, USERS_MOCK, tags);
        var ctx = new ContextMock();
        ctx.setHeaders(HEADERS);
        var dto = new TagDto();
        dto.setName("name");
        ctx.setBody(dto);
        ctx.status(HttpStatus.OK);
        controller.put(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals("name", ((Tag) ctx.getJson()).getName());
    }

    // Update
    @Test
    @SuppressWarnings("unchecked")
    public void testUpdateBadAuth() {
        var supplier = (Supplier<ContextMock>) () -> {
            var ctx = new ContextMock();
            var dto = new TagDto();
            dto.setName("");
            ctx.setBody(dto);
            return ctx;
        };
        testBadAuth((p, us) -> new TagController((JwtProvider<JwtUser>) p, us, null)::update, supplier);
    }

    @Test
    public void testUpdateInvalidBody() {
        var controller = new TagController(null, null, null);
        testInvalidBody(controller, c -> c::update);
    }

    @Test
    public void testUpdateNotFound() {
        var tags = new RepositoryMock<Tag>();
        var controller = new TagController(PROVIDER_MOCK, USERS_MOCK, tags);
        var ctx = new ContextMock();
        var dto = new TagDto();
        dto.setName("1");
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("tagId", ""));
        ctx.setBody(dto);
        controller.update(ctx);
        assertEquals(HttpStatus.NOT_FOUND, ctx.status());
    }

    @Test
    public void testUpdate() {
        var tags = new RepositoryMock<Tag>() {
            Tag tag;

            @Override
            public Tag get(String key) {
                return new Tag();
            }

            @Override
            public long update(String key, Tag model) {
                tag = model;
                return 1;
            }
        };
        var controller = new TagController(PROVIDER_MOCK, USERS_MOCK, tags);
        var ctx = new ContextMock();
        var dto = new TagDto();
        dto.setName("1");
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("tagId", ""));
        ctx.setBody(dto);
        controller.update(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals("1", tags.tag.getName());
    }

    // Delete
    @Test
    @SuppressWarnings("unchecked")
    public void testDeleteBadAuth() {
        var supplier = (Supplier<ContextMock>) () -> {
            var ctx = new ContextMock();
            var dto = new TagDto();
            dto.setName("");
            ctx.setBody(dto);
            return ctx;
        };
        testBadAuth((p, us) -> new TagController((JwtProvider<JwtUser>) p, us, null)::delete, supplier);
    }

    @Test
    public void testDeleteNotFound() {
        var tags = new RepositoryMock<Tag>();
        var controller = new TagController(PROVIDER_MOCK, USERS_MOCK, tags);
        var ctx = new ContextMock();
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("tagId", ""));
        controller.delete(ctx);
        assertEquals(HttpStatus.NOT_FOUND, ctx.status());
    }

    @Test
    public void testDelete() {
        var tags = new RepositoryMock<Tag>() {

            @Override
            public boolean delete(String key) {
                assertEquals("1", key);
                return true;
            }
        };
        var controller = new TagController(PROVIDER_MOCK, USERS_MOCK, tags);
        var ctx = new ContextMock();
        ctx.status(HttpStatus.OK);
        ctx.setHeaders(HEADERS);
        ctx.setPathParams(Map.of("tagId", "1"));
        controller.delete(ctx);
        assertEquals(HttpStatus.OK, ctx.status());
    }
}
