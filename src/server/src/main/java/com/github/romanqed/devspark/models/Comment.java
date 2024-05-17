package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Repository;

import java.util.*;

@Model("comments")
public final class Comment extends Owned implements Rated {
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
        ret.scores = new HashMap<>();
        var now = new Date();
        ret.created = now;
        ret.updated = now;
        return ret;
    }

    public static boolean delete(Repository<Comment> repository, String userId, String commentId) {
        var fields = Map.<String, Object>of(
                "_id", commentId,
                "ownerId", userId
        );
        return repository.delete(fields);
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

    public int calculateScore() {
        var ret = 0;
        for (var score : scores.values()) {
            ret += score;
        }
        return ret;
    }

    public Post retrievePost(Repository<Post> repository) {
        return repository.get(postId);
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
