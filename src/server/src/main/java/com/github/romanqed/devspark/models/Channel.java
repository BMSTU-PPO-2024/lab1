package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import org.bson.types.ObjectId;

@Model("channels")
public final class Channel {
    private ObjectId id;
    private String name;

}
