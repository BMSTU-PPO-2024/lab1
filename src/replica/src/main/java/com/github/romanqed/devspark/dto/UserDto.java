package com.github.romanqed.devspark.dto;

import com.github.romanqed.devspark.models.User;
import com.github.romanqed.jfunc.Exceptions;

import java.net.URL;
import java.util.Date;

public final class UserDto {
    private String id;
    private String nickname;
    private String email;
    private String password;
    private Boolean banned;
    private String about;
    private URL avatar;
    private Integer permissions;
    private Date created;
    private Date updated;

    public static UserDto of(User user) {
        var ret = new UserDto();
        ret.id = user.getId();
        ret.nickname = user.getNickname();
        ret.email = user.getEmail();
        ret.banned = user.isBanned();
        ret.about = user.getAbout();
        var avatar = user.getAvatar();
        if (avatar != null) {
            ret.avatar = Exceptions.suppress(() -> new URL(user.getAvatar()));
        }
        return ret;
    }

    public static UserDto ofAll(User user) {
        var ret = of(user);
        ret.created = user.getCreated();
        ret.updated = user.getUpdated();
        ret.permissions = user.getPermissions();
        return ret;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPermissions() {
        return permissions;
    }

    public void setPermissions(Integer permissions) {
        this.permissions = permissions;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public URL getAvatar() {
        return avatar;
    }

    public void setAvatar(URL avatar) {
        this.avatar = avatar;
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
}
