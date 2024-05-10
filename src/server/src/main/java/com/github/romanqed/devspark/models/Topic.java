package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Repository;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.HashSet;
import java.util.Set;

@Model("topics")
public final class Topic {
    private ObjectId id;
    private String name;
    private Set<ObjectId> tagIds;
    // Tag Models
    private transient Set<Tag> tags;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ObjectId> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<ObjectId> tagIds) {
        this.tagIds = tagIds;
    }

    public void addTags(Set<ObjectId> ids, Repository<ObjectId, Tag, Bson> repository) {
        if (!Tag.exists(ids, repository)) {
            throw new IllegalArgumentException("Invalid tag ids");
        }
        this.tagIds.addAll(ids);
    }

    public void removeTags(Set<ObjectId> ids) {
        this.tagIds.removeAll(ids);
    }

    public void retrieveTags(Repository<ObjectId, Tag, ?> repository) {
        tags = new HashSet<>();
        var found = repository.findAll(tagIds);
        found.forEach(tags::add);
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
