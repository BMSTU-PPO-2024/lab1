package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Model("tags")
public final class Tag {
    private String id;
    private String name;
    private Date created;
    private Date updated;

    public static Tag of(String name) {
        var ret = new Tag();
        ret.id = UUID.randomUUID().toString();
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        var tag = (Tag) object;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
