package com.github.romanqed.devspark.dto;

import java.util.Set;

public final class TopicDto implements Validated {
    private String name;
    private Set<String> tagIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
            throw new ValidateException("Tag name is null");
        }
        if (tagIds == null) {
            throw new ValidateException("Tag set is null");
        }
        tagIds.forEach(id -> {
            if (id == null) {
                throw new ValidateException("Tag id is null");
            }
        });
    }
}
