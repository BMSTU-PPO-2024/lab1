package com.github.romanqed.devspark.dto;

public final class TextDto implements Validated {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void validate() throws ValidateException {
        if (text == null) {
            throw new ValidateException("Test is null");
        }
    }
}
