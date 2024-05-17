package com.github.romanqed.devspark.dto;

import com.github.romanqed.devspark.models.Privacy;

import java.util.Set;

public final class PostDto implements Validated {
    private String title;
    private String text;
    private Privacy privacy;
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

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
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
        if (tagIds == null) {
            throw new ValidateException("Tag set is null");
        }
    }
}
