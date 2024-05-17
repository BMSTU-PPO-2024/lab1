package com.github.romanqed.devspark.dto;

import com.github.romanqed.devspark.models.Privacy;

public final class ChannelDto implements Validated {
    private String name;
    private Privacy privacy;

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

    @Override
    public void validate() throws ValidateException {
        if (name == null) {
            throw new ValidateException("Empty name");
        }
    }
}
