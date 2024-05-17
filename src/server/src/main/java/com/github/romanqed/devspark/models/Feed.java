package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Model("feeds")
public final class Feed extends Owned {
    private String id;
    private String name;
    private Privacy privacy;
    private Set<String> channelIds;
    private Set<String> topicIds;
    private Set<String> tagIds;
    private Date created;
    private Date updated;

    public static Feed of(String owner, String name) {
        var ret = new Feed();
        ret.id = UUID.randomUUID().toString();
        ret.name = Objects.requireNonNull(name);
        ret.ownerId = Objects.requireNonNull(owner);
        ret.privacy = Privacy.PRIVATE;
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

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public Set<String> getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(Set<String> channelIds) {
        this.channelIds = channelIds;
    }

    public Set<String> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(Set<String> topicIds) {
        this.topicIds = topicIds;
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
}
