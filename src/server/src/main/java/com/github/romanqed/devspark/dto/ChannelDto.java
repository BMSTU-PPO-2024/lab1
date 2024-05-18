package com.github.romanqed.devspark.dto;

public final class ChannelDto implements Validated {
    private String name;
    private Boolean visible;

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

    @Override
    public void validate() throws ValidateException {
        if (name == null) {
            throw new ValidateException("Empty name");
        }
    }
}
