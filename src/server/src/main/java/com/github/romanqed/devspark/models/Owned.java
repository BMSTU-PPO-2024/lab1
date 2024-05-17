package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Repository;

public class Owned {
    String ownerId;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public User retrieveOwner(Repository<User> repository) {
        return repository.get(ownerId);
    }

    public boolean isOwnedBy(User user) {
        return user != null && ownerId.equals(user.getId());
    }
}
