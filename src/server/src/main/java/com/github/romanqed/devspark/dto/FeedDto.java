package com.github.romanqed.devspark.dto;

import com.github.romanqed.devspark.models.Privacy;

import java.util.Set;

public final class FeedDto implements Validated {
    private String name;
    private Privacy privacy;
    private Set<String> channelIds;
    private Set<String> topicIds;
    private Set<String> tagIds;

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

    @Override
    public void validate() throws ValidateException {
        if (name == null) {
            throw new ValidateException("Name is null");
        }
    }
}
