package com.github.romanqed.devspark.commands;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.CliCommand;
import com.github.romanqed.devspark.javalin.TextCommand;
import com.github.romanqed.devspark.models.Channel;
import com.github.romanqed.devspark.models.Comment;
import com.github.romanqed.devspark.models.Post;

@CliCommand("del-channel")
public final class DeleteChannelCommand implements TextCommand {
    private final Repository<Channel> channels;
    private final Repository<Post> posts;
    private final Repository<Comment> comments;

    public DeleteChannelCommand(Repository<Channel> channels, Repository<Post> posts, Repository<Comment> comments) {
        this.channels = channels;
        this.posts = posts;
        this.comments = comments;
    }

    @Override
    public void execute(String[] arguments) {
        System.out.println(Channel.delete(channels, posts, comments, arguments[0]));
    }
}
