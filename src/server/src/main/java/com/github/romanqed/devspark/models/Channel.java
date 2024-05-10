package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import org.bson.types.ObjectId;

@Model("channels")
public final class Channel extends Owned {
    private ObjectId id;
    private String name;
    private Privacy privacy;

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

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }
}
