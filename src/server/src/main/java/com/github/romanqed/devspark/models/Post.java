package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Repository;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Model("posts")
public final class Post extends Owned {
    private ObjectId id;
    private String title;
    private String text;
    private Set<ObjectId> tagIds;
    private Map<ObjectId, Integer> scores;
    // Tag Models
    private transient Set<Tag> tags;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<ObjectId> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<ObjectId> tagIds) {
        this.tagIds = tagIds;
    }

    public Map<ObjectId, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<ObjectId, Integer> scores) {
        this.scores = scores;
    }

    public int calculateScore() {
        var ret = 0;
        for (var score : scores.values()) {
            ret += score;
        }
        return ret;
    }

    public void addTags(Set<ObjectId> ids, Repository<ObjectId, Tag, Bson> repository) {
        if (!repository.exists(ids)) {
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
        return Collections.unmodifiableSet(tags);
    }
}
