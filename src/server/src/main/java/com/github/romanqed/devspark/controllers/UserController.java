package com.github.romanqed.devspark.controllers;

import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.dto.DtoUtil;
import com.github.romanqed.devspark.dto.Response;
import com.github.romanqed.devspark.dto.UserDto;
import com.github.romanqed.devspark.hash.Encoder;
import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import com.github.romanqed.devspark.jwt.JwtProvider;
import com.github.romanqed.devspark.jwt.JwtUser;
import com.github.romanqed.devspark.models.*;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;

import java.util.Date;

@JavalinController("/user")
public final class UserController extends AuthBase {
    private final Encoder encoder;
    private final Repository<Channel> channels;
    private final Repository<Feed> feeds;

    public UserController(JwtProvider<JwtUser> provider,
                          Repository<User> users,
                          Repository<Channel> channels,
                          Repository<Feed> feeds,
                          Encoder encoder) {
        super(provider, users);
        this.channels = channels;
        this.feeds = feeds;
        this.encoder = encoder;
    }

    @Route(method = HandlerType.GET)
    public void getSelf(Context ctx) {
        var user = getCheckedFullUser(ctx);
        if (user == null) {
            return;
        }
        ctx.json(UserDto.ofAll(user));
    }

    @Route(method = HandlerType.GET, route = "/{userId}")
    public void get(Context ctx) {
        var id = ctx.pathParam("userId");
        var found = users.get(id);
        if (found == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        var user = getUser(ctx);
        var all = user != null && !user.isBanned()
                && (id.equals(user.getId()) || user.hasPermission(Permissions.MANAGE_USERS));
        if (all) {
            ctx.json(UserDto.ofAll(found));
            return;
        }
        ctx.json(UserDto.of(found));
    }

    private boolean updateUser(UserDto dto, User user) {
        var ret = false;
        // Update nickname
        var nickname = dto.getNickname();
        if (nickname != null) {
            ret = true;
            user.setNickname(nickname);
        }
        // Update password
        var password = dto.getPassword();
        if (password != null) {
            ret = true;
            user.setPassword(encoder.encode(password));
        }
        // Update about
        var about = dto.getAbout();
        if (about != null) {
            ret = true;
            user.setAbout(about);
        }
        // Update avatar
        var avatar = dto.getAvatar();
        if (avatar != null) {
            ret = true;
            user.setAvatar(avatar.toString());
        }
        return ret;
    }

    @Route(method = HandlerType.PATCH)
    public void updateSelf(Context ctx) {
        var dto = DtoUtil.parse(ctx, UserDto.class);
        if (dto == null) {
            return;
        }
        var user = getCheckedFullUser(ctx);
        if (user == null) {
            return;
        }
        if (!updateUser(dto, user)) {
            ctx.status(HttpStatus.BAD_REQUEST);
            return;
        }
        user.setUpdated(new Date());
        users.update(user.getId(), user);
    }

    @Route(method = HandlerType.PATCH, route = "/{userId}")
    public void update(Context ctx) {
        var dto = DtoUtil.parse(ctx, UserDto.class);
        if (dto == null) {
            return;
        }
        if (!validatePermission(ctx, Permissions.MANAGE_USERS)) {
            return;
        }
        var user = users.get(ctx.pathParam("userId"));
        if (user == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        var check = updateUser(dto, user);
        // Update permissions
        var permissions = dto.getPermissions();
        if (permissions != null) {
            check = true;
            user.setPermissions(permissions);
        }
        // Update banned
        var banned = dto.isBanned();
        if (banned != null) {
            check = true;
            user.setBanned(banned);
        }
        if (!check) {
            ctx.status(HttpStatus.BAD_REQUEST);
            return;
        }
        user.setUpdated(new Date());
        users.update(user.getId(), user);
    }

    private void list(Context ctx, Repository<?> repository, String userId, boolean all, Pagination pagination) {
        var raw = ctx.queryParam("pattern");
        if (raw != null) {
            var pattern = Util.checkPattern(ctx, raw);
            if (pattern == null) {
                return;
            }
            var found = ModelUtil.matchByUserId(repository, userId, pattern, all, pagination);
            ctx.json(found);
            return;
        }
        var name = ctx.queryParam("name");
        var found = ModelUtil.findByUserId(repository, userId, name, all, pagination);
        ctx.json(found);
    }

    private void listSelf(Context ctx, Repository<?> repository) {
        var pagination = DtoUtil.parsePagination(ctx);
        if (pagination == null) {
            return;
        }
        var user = getCheckedUser(ctx);
        if (user == null) {
            return;
        }
        list(ctx, repository, user.getId(), true, pagination);
    }

    private void listUser(Context ctx, Repository<?> repository) {
        var pagination = DtoUtil.parsePagination(ctx);
        if (pagination == null) {
            return;
        }
        var id = ctx.pathParam("userId");
        if (!users.exists(id)) {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(new Response("User not found"));
            return;
        }
        var user = getUser(ctx);
        var all = user != null
                && !user.isBanned()
                && (id.equals(user.getId()) || user.hasPermission(Permissions.IGNORE_VISIBILITY));
        list(ctx, repository, id, all, pagination);
    }

    // List channels
    @Route(method = HandlerType.GET, route = "/channels")
    public void listSelfChannels(Context ctx) {
        listSelf(ctx, channels);
    }

    @Route(method = HandlerType.GET, route = "/{userId}/channels")
    public void listChannels(Context ctx) {
        listUser(ctx, channels);
    }

    // List feeds
    @Route(method = HandlerType.GET, route = "/feeds")
    public void listSelfFeeds(Context ctx) {
        listSelf(ctx, feeds);
    }

    @Route(method = HandlerType.GET, route = "/{userId}/feeds")
    public void listFeeds(Context ctx) {
        listUser(ctx, feeds);
    }
}
