package com.github.romanqed.devspark.dto;

public final class TagDto implements Validated {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void validate() throws ValidateException {
        if (name == null) {
            throw new ValidateException("Tag name is null");
        }
    }
}
