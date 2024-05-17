package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;

import java.util.*;

@Model("topics")
public final class Topic {
    private String id;
    private String name;
    private Set<String> tagIds;
    private Date created;
    private Date updated;

    public static Topic of(String name, Set<String> tagIds) {
        var ret = new Topic();
        ret.id = UUID.randomUUID().toString();
        ret.name = Objects.requireNonNull(name);
        ret.tagIds = Objects.requireNonNull(tagIds);
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

    public Set<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<String> tagIds) {
        this.tagIds = tagIds;
    }

    public void removeTags(Set<String> ids) {
        this.tagIds.removeAll(ids);
    }

    public Set<Tag> retrieveTags(Repository<Tag> repository, Pagination pagination) {
        var ret = new HashSet<Tag>();
        var found = repository.getAll(tagIds, pagination);
        found.forEach(ret::add);
        return ret;
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
