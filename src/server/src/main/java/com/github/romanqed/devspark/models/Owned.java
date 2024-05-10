package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Repository;
import org.bson.types.ObjectId;

class Owned {
    ObjectId ownerId;
    transient User owner;

    public ObjectId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(ObjectId ownerId) {
        this.ownerId = ownerId;
    }

    public void retrieveOwner(Repository<ObjectId, User, ?> repository) {
        this.owner = repository.find(ownerId);
    }

    public User getOwner(Repository<ObjectId, User, ?> repository) {
        if (owner == null) {
            retrieveOwner(repository);
        }
        return owner;
    }

    public User getOwner() {
        return owner;
    }
}
