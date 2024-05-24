package com.github.romanqed.devspark.unit;

import com.github.romanqed.devspark.controllers.AuthController;
import com.github.romanqed.devspark.dto.Credentials;
import com.github.romanqed.devspark.dto.Response;
import com.github.romanqed.devspark.dto.Token;
import com.github.romanqed.devspark.hash.Encoder;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.mocks.ContextMock;
import com.github.romanqed.devspark.mocks.EncoderMock;
import com.github.romanqed.devspark.mocks.JwtProviderMock;
import com.github.romanqed.devspark.mocks.RepositoryMock;
import com.github.romanqed.devspark.models.User;
import io.javalin.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class AuthControllerTest {
    private static final Encoder ENCODER = new EncoderMock();

    // Register tests
    @Test
    public void testMissingBodyAtRegister() {
        var controller = new AuthController(null, null, null);
        var ctx = new ContextMock();
        ctx.setBody(null);
        controller.register(ctx);
        assertEquals(HttpStatus.BAD_REQUEST, ctx.status());
        assertEquals(((Response) ctx.getJson()).getMessage(), "Missing body");
    }

    @Test
    public void testMissingEmailAtRegister() {
        var controller = new AuthController(null, null, null);
        var ctx = new ContextMock();
        var creds = new Credentials();
        creds.setEmail(null);
        creds.setPassword("");
        ctx.setBody(creds);
        controller.register(ctx);
        assertEquals(HttpStatus.BAD_REQUEST, ctx.status());
        assertEquals(((Response) ctx.getJson()).getMessage(), "Missing email");
    }

    @Test
    public void testInvalidEmailAtRegister() {
        var controller = new AuthController(null, null, null);
        var ctx = new ContextMock();
        var creds = new Credentials();
        creds.setEmail("it's not email");
        creds.setPassword("");
        ctx.setBody(creds);
        controller.register(ctx);
        assertEquals(HttpStatus.BAD_REQUEST, ctx.status());
        assertEquals(((Response) ctx.getJson()).getMessage(), "Invalid email");
    }

    @Test
    public void testMissingPasswordAtRegister() {
        var controller = new AuthController(null, null, null);
        var ctx = new ContextMock();
        var creds = new Credentials();
        creds.setEmail("email@email.e");
        creds.setPassword(null);
        ctx.setBody(creds);
        controller.register(ctx);
        assertEquals(HttpStatus.BAD_REQUEST, ctx.status());
        assertEquals(((Response) ctx.getJson()).getMessage(), "Missing password");
    }

    @Test
    public void testMissingBodyAtLogin() {
        var controller = new AuthController(null, null, null);
        var ctx = new ContextMock();
        ctx.setBody(null);
        controller.login(ctx);
        assertEquals(HttpStatus.BAD_REQUEST, ctx.status());
        assertEquals(((Response) ctx.getJson()).getMessage(), "Missing body");
    }

    @Test
    public void testMissingEmailAtLogin() {
        var controller = new AuthController(null, null, null);
        var ctx = new ContextMock();
        var creds = new Credentials();
        creds.setEmail(null);
        creds.setPassword("");
        ctx.setBody(creds);
        controller.login(ctx);
        assertEquals(HttpStatus.BAD_REQUEST, ctx.status());
        assertEquals(((Response) ctx.getJson()).getMessage(), "Missing email");
    }

    @Test
    public void testInvalidEmailAtLogin() {
        var controller = new AuthController(null, null, null);
        var ctx = new ContextMock();
        var creds = new Credentials();
        creds.setEmail("it's not email");
        creds.setPassword("");
        ctx.setBody(creds);
        controller.login(ctx);
        assertEquals(HttpStatus.BAD_REQUEST, ctx.status());
        assertEquals(((Response) ctx.getJson()).getMessage(), "Invalid email");
    }

    @Test
    public void testMissingPasswordAtLogin() {
        var controller = new AuthController(null, null, null);
        var ctx = new ContextMock();
        var creds = new Credentials();
        creds.setEmail("email@email.e");
        creds.setPassword(null);
        ctx.setBody(creds);
        controller.login(ctx);
        assertEquals(HttpStatus.BAD_REQUEST, ctx.status());
        assertEquals(((Response) ctx.getJson()).getMessage(), "Missing password");
    }

    @Test
    public void testUserExistsAtRegister() {
        var users = new RepositoryMock<User>() {

            @Override
            public boolean exists(String field, Object value) {
                return true;
            }
        };
        var controller = new AuthController(users, null, null);
        var ctx = new ContextMock();
        var creds = new Credentials();
        creds.setEmail("email@email.e");
        creds.setPassword("");
        ctx.setBody(creds);
        controller.register(ctx);
        assertEquals(HttpStatus.CONFLICT, ctx.status());
    }

    @Test
    public void testRegister() {
        var users = new RepositoryMock<User>() {
            User user;

            @Override
            public long put(User model) {
                user = model;
                return 1;
            }
        };
        var provider = new JwtProviderMock<JwtUser>();
        var controller = new AuthController(users, provider, ENCODER);
        var ctx = new ContextMock();
        var creds = new Credentials();
        creds.setEmail("email@email.e");
        creds.setPassword("123");
        ctx.status(HttpStatus.OK);
        ctx.setBody(creds);
        controller.register(ctx);
        assertEquals(ctx.status(), HttpStatus.OK);
        var user = users.user;
        var token = (Token) ctx.getJson();
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals("token", token.getToken());
        assertEquals("email@email.e", user.getEmail());
        assertEquals("123", user.getPassword());
    }

    // Login tests
    @Test
    public void testUserNotFound() {
        var users = new RepositoryMock<User>();
        var controller = new AuthController(users, null, null);
        var ctx = new ContextMock();
        var creds = new Credentials();
        creds.setEmail("email@email.e");
        creds.setPassword("123");
        ctx.setBody(creds);
        controller.login(ctx);
        assertEquals(HttpStatus.NOT_FOUND, ctx.status());
    }

    @Test
    public void testInvalidPassword() {
        var users = new RepositoryMock<User>() {

            @Override
            public User findFirstByField(String field, Object value) {
                var user = new User();
                user.setPassword("");
                return user;
            }
        };
        var controller = new AuthController(users, null, ENCODER);
        var ctx = new ContextMock();
        var creds = new Credentials();
        creds.setEmail("email@email.e");
        creds.setPassword("123");
        ctx.setBody(creds);
        controller.login(ctx);
        assertEquals(HttpStatus.UNAUTHORIZED, ctx.status());
    }

    @Test
    public void testUserIsBanned() {
        var users = new RepositoryMock<User>() {

            @Override
            public User findFirstByField(String field, Object value) {
                var user = new User();
                user.setEmail("email@email.e");
                user.setPassword("123");
                return user;
            }
        };
        var provider = new JwtProviderMock<JwtUser>();
        var controller = new AuthController(users, provider, ENCODER);
        var ctx = new ContextMock();
        var creds = new Credentials();
        creds.setEmail("email@email.e");
        creds.setPassword("123");
        ctx.status(HttpStatus.OK);
        ctx.setBody(creds);
        controller.login(ctx);
        var token = (Token) ctx.getJson();
        assertEquals(HttpStatus.OK, ctx.status());
        assertEquals("token", token.getToken());
        var ju = provider.getTokenObj();
        assertEquals("email@email.e", ju.getEmail());
    }
}
