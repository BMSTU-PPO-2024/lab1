package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Repository;

import java.util.*;

@Model("posts")
public final class Post extends Owned implements Rated {
    private String id;
    private String title;
    private String text;
    private Privacy privacy;
    private Set<String> tagIds;
    private Map<String, Integer> scores;
    private Date created;
    private Date updated;

    // Tag Models
    private transient Set<Tag> tags;

    public static Post of(String owner, String title, String text) {
        var ret = new Post();
        ret.id = UUID.randomUUID().toString();
        ret.title = Objects.requireNonNull(title);
        ret.text = Objects.requireNonNull(text);
        ret.privacy = Privacy.PUBLIC;
        ret.ownerId = Objects.requireNonNull(owner);
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

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public Set<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<String> tagIds) {
        this.tagIds = tagIds;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public int calculateScore() {
        var ret = 0;
        for (var score : scores.values()) {
            ret += score;
        }
        return ret;
    }

    public void addTags(Set<String> ids, Repository<Tag> repository) {
        if (!repository.exists(ids)) {
            throw new IllegalArgumentException("Invalid tag ids");
        }
        this.tagIds.addAll(ids);
    }

    public void removeTags(Set<String> ids) {
        this.tagIds.removeAll(ids);
    }

    public void retrieveTags(Repository<Tag> repository) {
        tags = new HashSet<>();
        var found = repository.getAll(tagIds);
        found.forEach(tags::add);
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
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
    public boolean rate(User user, int value) {
        var id = user.getId();
        if (this.scores.containsKey(id)) {
            return false;
        }
        this.scores.put(id, value);
        return true;
    }

    @Override
    public boolean unrate(User user) {
        return this.scores.remove(user.getId()) != null;
    }
}
