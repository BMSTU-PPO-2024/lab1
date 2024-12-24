package com.github.romanqed.devspark.dto;

import java.util.Set;

public final class PostDto implements Validated {
    private String title;
    private String text;
    private Boolean visible;
    private Set<String> tagIds;

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

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Set<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<String> tagIds) {
        this.tagIds = tagIds;
    }

    @Override
    public void validate() throws ValidateException {
        if (title == null) {
            throw new ValidateException("Title is null");
        }
        if (text == null) {
            throw new ValidateException("Text is null");
        }
    }
}