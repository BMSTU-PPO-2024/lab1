package com.github.romanqed.devspark.models;

public interface Rated {
    boolean rate(User user, int value);

    boolean unrate(User user);
}
