package com.github.romanqed.devspark.dto;

import com.github.romanqed.devspark.models.Comment;

import java.util.Date;
import java.util.Map;

public final class CommentDto {
    private String id;
    private String postId;
    private String text;
    private Map<String, Integer> scores;
    private Date created;
    private Date updated;

    public static CommentDto of(Comment comment) {
        var ret = new CommentDto();
        ret.id = comment.getId();
        ret.postId = comment.getPostId();
        ret.text = comment.getText();
        ret.scores = comment.getScores();
        ret.created = comment.getCreated();
        ret.updated = comment.getUpdated();
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
