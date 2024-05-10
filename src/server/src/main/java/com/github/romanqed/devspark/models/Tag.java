package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Repository;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Model("tags")
public final class Tag {
    private ObjectId id;
    private String name;

    public static Set<Tag> getTagsByName(Set<String> tags, Repository<ObjectId, Tag, Bson> repository) {
        var found = repository.filter(Filters.in("name", tags));
        var ret = new HashSet<Tag>();
        found.forEach(ret::add);
        return ret;
    }

    public static boolean exists(Set<ObjectId> ids, Repository<ObjectId, Tag, Bson> repository) {
        var result = repository.count(Filters.in("_id", ids));
        return result == ids.size();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        var tag = (Tag) object;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
