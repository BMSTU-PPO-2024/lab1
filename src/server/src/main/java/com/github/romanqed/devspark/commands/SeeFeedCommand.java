package com.github.romanqed.devspark.commands;

import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;
import com.github.romanqed.devspark.javalin.CliCommand;
import com.github.romanqed.devspark.javalin.TextCommand;
import com.github.romanqed.devspark.models.Feed;
import com.github.romanqed.devspark.models.Post;

@CliCommand("see-feed")
public final class SeeFeedCommand implements TextCommand {
    private final Repository<Post> posts;
    private final Repository<Feed> feeds;

    public SeeFeedCommand(Repository<Post> posts, Repository<Feed> feeds) {
        this.posts = posts;
        this.feeds = feeds;
    }

    @Override
    public void execute(String[] arguments) {
        var found = feeds.get(arguments[0]);
        if (found == null) {
            System.out.println("Not found");
            return;
        }
        var pagination = new Pagination(Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2]));
        System.out.println(found.getPosts(posts, pagination));
    }
}
