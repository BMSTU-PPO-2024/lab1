package com.github.romanqed.devspark.dto;

import java.util.Set;

public final class FeedDto implements Validated {
    private String name;
    private Boolean visible;
    private Set<String> channelIds;
    private Set<String> tagIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
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

    @Override
    public void validate() throws ValidateException {
        if (name == null) {
            throw new ValidateException("Name is null");
        }
    }
}
