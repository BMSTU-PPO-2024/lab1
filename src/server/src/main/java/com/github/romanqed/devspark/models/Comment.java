package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Repository;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Model("comments")
public final class Comment extends Owned {
    // Post model
    Post post;
    private String id;
    private String postId;
    private String text;
    private Map<String, Integer> scores;
    private Date created;
    private Date updated;

    public static Comment of(String owner, String postId, String text) {
        var ret = new Comment();
        ret.id = UUID.randomUUID().toString();
        ret.ownerId = Objects.requireNonNull(owner);
        ret.postId = Objects.requireNonNull(postId);
        ret.text = Objects.requireNonNull(text);
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public void retrievePost(Repository<Post> repository) {
        this.post = repository.get(postId);
    }

    public Post getPost(Repository<Post> repository) {
        if (post == null) {
            retrievePost(repository);
        }
        return post;
    }

    public Post getPost() {
        return post;
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
