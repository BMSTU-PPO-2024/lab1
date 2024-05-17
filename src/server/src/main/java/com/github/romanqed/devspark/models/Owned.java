package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Repository;

class Owned {
    String ownerId;
    transient User owner;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void retrieveOwner(Repository<User> repository) {
        this.owner = repository.get(ownerId);
    }

    public User getOwner(Repository<User> repository) {
        if (owner == null) {
            retrieveOwner(repository);
        }
        return owner;
    }

    public User getOwner() {
        return owner;
    }
}
