package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Repository;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Model("users")
public final class User implements Identified {
    private static final List<String> AUTH_FIELDS = List.of("_id", "banned", "email", "password", "permissions");

    private String id;
    private String nickname;
    private String email;
    private boolean banned;
    private String password;
    private String about;
    private String avatar;
    private int permissions;
    private Date created;
    private Date updated;

    public static User of(String email, String password) {
        var ret = new User();
        ret.id = UUID.randomUUID().toString();
        ret.email = Objects.requireNonNull(email);
        ret.password = Objects.requireNonNull(password);
        var now = new Date();
        ret.created = now;
        ret.updated = now;
        return ret;
    }

    public static User getAuthUser(Repository<User> users, String id) {
        return users.get(id, AUTH_FIELDS);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void grantPermission(Permissions permission) {
        this.permissions |= permission.value;
    }

    public void revokePermission(Permissions permission) {
        this.permissions &= ~permission.value;
    }

    public boolean hasPermission(Permissions permission) {
        var value = permission.value;
        return (this.permissions & value) == value;
    }
}
