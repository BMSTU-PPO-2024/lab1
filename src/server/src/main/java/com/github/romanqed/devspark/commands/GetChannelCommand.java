package com.github.romanqed.devspark.commands;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.CliCommand;
import com.github.romanqed.devspark.javalin.TextCommand;
import com.github.romanqed.devspark.models.Channel;

@CliCommand("get-channel")
public final class GetChannelCommand implements TextCommand {
    private final Repository<Channel> channels;

    public GetChannelCommand(Repository<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public void execute(String[] arguments) {
        System.out.println(channels.get(arguments[0]));
    }
}
