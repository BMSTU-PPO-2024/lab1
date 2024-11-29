package com.github.romanqed.devspark.commands;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.CliCommand;
import com.github.romanqed.devspark.javalin.TextCommand;
import com.github.romanqed.devspark.models.User;

@CliCommand("put-user")
public final class PutUserCommand implements TextCommand {
    private final Repository<User> users;

    public PutUserCommand(Repository<User> users) {
        this.users = users;
    }

    @Override
    public void execute(String[] arguments) {
        var user = User.of(arguments[0], arguments[1]);
        user.setPermissions(Integer.parseInt(arguments[2]));
        users.put(user);
    }
}
