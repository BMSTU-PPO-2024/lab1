package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;

import java.util.*;

@Model("posts")
public final class Post extends Owned implements Rated {
    private String id;
    private String title;
    private String text;
    private String channelId;
    private Set<String> tagIds;
    private Map<String, Integer> scores;
    private Date created;
    private Date updated;

    public static Post of(String owner, String channel, String title, String text) {
        var ret = new Post();
        ret.id = UUID.randomUUID().toString();
        ret.title = Objects.requireNonNull(title);
        ret.text = Objects.requireNonNull(text);
        ret.ownerId = Objects.requireNonNull(owner);
        ret.channelId = Objects.requireNonNull(channel);
        var now = new Date();
        ret.created = now;
        ret.updated = now;
        return ret;
    }

    public static boolean delete(String postId, Repository<Post> posts, Repository<Comment> comments) {
        if (!posts.delete(postId)) {
            return false;
        }
        comments.deleteAll("postId", postId);
        return true;
    }

    public static boolean delete(String userId, String postId, Repository<Post> posts, Repository<Comment> comments) {
        var fields = Map.<String, Object>of(
                "id", postId,
                "ownerId", userId
        );
        if (!posts.delete(fields)) {
            return false;
        }
        comments.deleteAll("postId", postId);
        return true;
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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public void removeTags(Set<String> ids) {
        this.tagIds.removeAll(ids);
    }

    public Set<Tag> retrieveTags(Repository<Tag> repository, Pagination pagination) {
        var ret = new HashSet<Tag>();
        var found = repository.getAll(tagIds, pagination);
        found.forEach(ret::add);
        return ret;
    }

    public List<Comment> retrieveComments(Repository<Comment> comments, Pagination pagination) {
        var ret = new LinkedList<Comment>();
        var found = comments.findByField("postId", id, pagination);
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

    public boolean isVisible(Repository<Channel> channels) {
        return channels.exists(Map.of(
                "id", channelId,
                "privacy", Privacy.PUBLIC
        ));
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
