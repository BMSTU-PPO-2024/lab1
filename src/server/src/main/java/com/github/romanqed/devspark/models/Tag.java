package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Repository;
import com.mongodb.client.model.Filters;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Model("tags")
public final class Tag {
    private String id;
    private String name;
    private Date created;
    private Date updated;

    public static Set<Tag> getTagsByName(Set<String> tags, Repository<Tag> repository) {
        var found = repository.filter(Filters.in("name", tags));
        var ret = new HashSet<Tag>();
        found.forEach(ret::add);
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
