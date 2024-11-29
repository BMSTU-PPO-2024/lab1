package com.github.romanqed.devspark.commands;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.CliCommand;
import com.github.romanqed.devspark.javalin.TextCommand;
import com.github.romanqed.devspark.models.Post;

import java.util.Set;

@CliCommand("publish-post")
public final class PublishPostCommand implements TextCommand {
    private final Repository<Post> posts;

    public PublishPostCommand(Repository<Post> posts) {
        this.posts = posts;
    }

    @Override
    public void execute(String[] arguments) {
        var post = Post.of(arguments[0], arguments[1], arguments[2], arguments[3]);
        post.setTagIds(Set.of(arguments[4]));
        posts.put(post);
    }
}
