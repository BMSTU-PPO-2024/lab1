package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.github.romanqed.devspark.CollectionUtil.asList;

public final class ModelUtil {
    private ModelUtil() {
    }

    public static <T> List<T> findByUserId(Repository<T> repository,
                                           String userId,
                                           String name,
                                           boolean all,
                                           Pagination pagination) {
        var fields = new HashMap<String, Object>();
        fields.put("ownerId", userId);
        if (name != null) {
            fields.put("name", name);
        }
        if (!all) {
            fields.put("privacy", Privacy.PUBLIC);
        }
        return asList(repository.findAnd(fields, pagination));
    }

    public static <T> List<T> matchByUserId(Repository<T> repository,
                                            String userId,
                                            Pattern pattern,
                                            boolean all,
                                            Pagination pagination) {
        var fields = new HashMap<String, Object>();
        fields.put("ownerId", userId);
        if (!all) {
            fields.put("privacy", Privacy.PUBLIC);
        }
        return asList(repository.findMatchedWithFields("name", pattern, fields, pagination));
    }

    public static <T> List<T> findAll(Repository<T> repository,
                                      String userId,
                                      boolean all,
                                      Pagination pagination) {
        if (all) {
            return asList(repository.getAll(pagination));
        }
        if (userId != null) {
            return asList(
                    repository.findOr(
                            Map.of("privacy", Privacy.PUBLIC),
                            Map.of("ownerId", userId),
                            pagination
                    )
            );
        }
        return asList(repository.findByField("privacy", Privacy.PUBLIC, pagination));
    }

    public static <T> List<T> findAllByName(Repository<T> repository,
                                            String userId,
                                            String name,
                                            boolean all,
                                            Pagination pagination) {
        if (all) {
            return asList(repository.findByField("name", name, pagination));
        }
        if (userId != null) {
            return asList(
                    repository.findOr(
                            Map.of(
                                    "name", name,
                                    "privacy", Privacy.PUBLIC
                            ),
                            Map.of("ownerId", userId),
                            pagination
                    )
            );
        }
        return asList(repository.findAnd(
                Map.of(
                        "name", name,
                        "privacy", Privacy.PUBLIC
                ),
                pagination
        ));
    }

    public static <T> List<T> matchAllByName(Repository<T> repository,
                                             String userId,
                                             Pattern pattern,
                                             boolean all,
                                             Pagination pagination) {
        if (all) {
            return asList(repository.findMatched("name", pattern, pagination));
        }
        if (userId != null) {
            return asList(repository.findMatchedWithFields(
                    "name",
                    pattern,
                    Map.of("privacy", Privacy.PUBLIC),
                    Map.of("ownerId", userId),
                    pagination
            ));
        }
        return asList(repository.findMatchedWithFields(
                "name",
                pattern,
                Map.of("privacy", Privacy.PUBLIC),
                pagination
        ));
    }
}
