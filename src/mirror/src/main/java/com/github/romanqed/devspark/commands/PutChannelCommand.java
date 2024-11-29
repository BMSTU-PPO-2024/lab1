package com.github.romanqed.devspark.commands;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.CliCommand;
import com.github.romanqed.devspark.javalin.TextCommand;
import com.github.romanqed.devspark.models.Channel;

@CliCommand("put-channel")
public final class PutChannelCommand implements TextCommand {
    private final Repository<Channel> channels;

    public PutChannelCommand(Repository<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public void execute(String[] arguments) {
        var channel = Channel.of(arguments[0], arguments[1]);
        channel.setVisible(Boolean.parseBoolean(arguments[2]));
        channels.put(channel);
    }
}
