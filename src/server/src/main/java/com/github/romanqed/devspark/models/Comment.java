package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Repository;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Map;

@Model("comments")
public final class Comment extends Owned {
    // Post model
    Post post;
    private ObjectId id;
    private ObjectId postId;
    private String text;
    private Map<ObjectId, Integer> scores;
    private Date created;
    private Date updated;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getPostId() {
        return postId;
    }

    public void setPostId(ObjectId postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public void retrievePost(Repository<ObjectId, Post, ?> repository) {
        this.post = repository.find(postId);
    }

    public Post getPost(Repository<ObjectId, Post, ?> repository) {
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
