package com.github.romanqed.devspark.models;

public enum Permissions {
    MANAGE_USERS(0x00000001),
    MANAGE_TAGS(0x00000002),
    MANAGE_TOPICS(0x00000004),
    MANAGE_CHANNELS(0x00000008),
    MANAGE_POSTS(0x00000010),
    MANAGE_COMMENTS(0x00000020),
    MANAGE_FEEDS(0x00000040);

    final int value;

    Permissions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
