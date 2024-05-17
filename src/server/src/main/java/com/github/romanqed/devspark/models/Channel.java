package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Model("channels")
public final class Channel extends Owned {
    private String id;
    private String name;
    private Privacy privacy;
    private Date created;
    private Date updated;

    public static Channel of(String owner, String name) {
        var ret = new Channel();
        ret.id = UUID.randomUUID().toString();
        ret.ownerId = Objects.requireNonNull(owner);
        ret.name = Objects.requireNonNull(name);
        var now = new Date();
        ret.created = now;
        ret.updated = now;
        return ret;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
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
