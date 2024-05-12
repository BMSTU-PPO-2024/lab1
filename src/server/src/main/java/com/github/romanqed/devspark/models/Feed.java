package com.github.romanqed.devspark.models;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Set;

public final class Feed extends Owned {
    private ObjectId id;
    private String name;
    private Privacy privacy;
    private Set<ObjectId> channelIds;
    private Set<ObjectId> topicIds;
    private Set<ObjectId> tagIds;
    private Date created;
    private Date updated;

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

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public Set<ObjectId> getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(Set<ObjectId> channelIds) {
        this.channelIds = channelIds;
    }

    public Set<ObjectId> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(Set<ObjectId> topicIds) {
        this.topicIds = topicIds;
    }

    public Set<ObjectId> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<ObjectId> tagIds) {
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
