package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;

import java.util.*;

import static com.github.romanqed.devspark.CollectionUtil.asList;

@Model("feeds")
public final class Feed extends Owned implements Visible {
    private String id;
    private String name;
    private boolean visible;
    private Set<String> channelIds;
    private Set<String> tagIds;
    private Date created;
    private Date updated;

    public static boolean delete(Repository<Feed> feeds, String userId, String feedId) {
        var fields = Map.<String, Object>of(
                "_id", feedId,
                "ownerId", userId
        );
        return feeds.delete(fields);
    }

    public static Feed of(String owner, String name) {
        var ret = new Feed();
        ret.id = UUID.randomUUID().toString();
        ret.name = Objects.requireNonNull(name);
        ret.ownerId = Objects.requireNonNull(owner);
        ret.visible = false;
        ret.channelIds = new HashSet<>();
        ret.tagIds = new HashSet<>();
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

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Set<String> getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(Set<String> channelIds) {
        this.channelIds = channelIds;
    }

    public Set<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<String> tagIds) {
        this.tagIds = tagIds;
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

    @SuppressWarnings("unchecked")
    public List<Post> getPosts(Repository<Post> posts, Pagination pagination) {
        // If feed has no channels and tags, then select all public posts
        if (channelIds.isEmpty() && tagIds.isEmpty()) {
            return asList(posts.findByField("visible", true, pagination));
        }
        var ins = new HashMap<String, Iterable<?>>();
        if (!tagIds.isEmpty()) {
            ins.put("tagIds", this.tagIds);
        }
        if (!channelIds.isEmpty()) {
            ins.put("channelId", channelIds);
        }
        var map = (Map<String, Iterable<Object>>) (Map<?, ?>) ins;
        var found = posts.findByField(map, Map.of("visible", true), pagination);
        return asList(found);
    }
}
