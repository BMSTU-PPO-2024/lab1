package com.github.romanqed.devspark;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class CollectionUtil {
    private CollectionUtil() {
    }

    public static <T> List<T> asList(Iterable<T> iterable) {
        var ret = new LinkedList<T>();
        iterable.forEach(ret::add);
        return ret;
    }

    public static <T> Set<T> asSet(Iterable<T> iterable) {
        var ret = new HashSet<T>();
        iterable.forEach(ret::add);
        return ret;
    }
}
