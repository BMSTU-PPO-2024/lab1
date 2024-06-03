package com.github.romanqed.devspark.commands;

import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.CliCommand;
import com.github.romanqed.devspark.javalin.TextCommand;
import com.github.romanqed.devspark.models.Comment;
import com.github.romanqed.devspark.models.Post;

@CliCommand("del-post")
public final class DeletePostCommand implements TextCommand {
    private final Repository<Post> posts;
    private final Repository<Comment> comments;

    public DeletePostCommand(Repository<Post> posts, Repository<Comment> comments) {
        this.posts = posts;
        this.comments = comments;
    }

    @Override
    public void execute(String[] arguments) {
        System.out.println(Post.delete(comments, arguments[0], posts));
    }
}
