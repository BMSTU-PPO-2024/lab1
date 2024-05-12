package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.Date;

@Model("users")
public final class User {
    private ObjectId id;
    private String nickname;
    private String email;
    private String password;
    private String about;
    private URL avatar;
    private int permissions;
    private Date created;
    private Date updated;


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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
}
