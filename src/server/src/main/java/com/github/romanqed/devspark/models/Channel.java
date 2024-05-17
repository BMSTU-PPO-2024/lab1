package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;

import java.util.*;
import java.util.regex.Pattern;

@Model("channels")
public final class Channel extends Owned {
    private String id;
    private String name;
    private Privacy privacy;
    private Date created;
    private Date updated;

    public static Channel of(String owner, String name) {
        var ret = new Channel();
        ret.id = UUID.randomUUID().toString();
        ret.ownerId = Objects.requireNonNull(owner);
        ret.name = Objects.requireNonNull(name);
        ret.privacy = Privacy.PUBLIC;
        var now = new Date();
        ret.created = now;
        ret.updated = now;
        return ret;
    }

    public static List<Channel> findAll(boolean all, Repository<Channel> channels, Pagination pagination) {
        var found = (Iterable<Channel>) null;
        if (all) {
            found = channels.getAll(pagination);
        } else {
            found = channels.findByField("privacy", Privacy.PUBLIC, pagination);
        }
        var ret = new LinkedList<Channel>();
        found.forEach(ret::add);
        return ret;
    }

    public static List<Channel> findByName(String name,
                                           boolean all,
                                           Repository<Channel> channels,
                                           Pagination pagination) {
        var found = (Iterable<Channel>) null;
        if (all) {
            found = channels.findByField("name", name, pagination);
        } else {
            found = channels.findByField(
                    Map.of(
                            "name", name,
                            "privacy", Privacy.PUBLIC
                    ),
                    pagination
            );
        }
        var ret = new LinkedList<Channel>();
        found.forEach(ret::add);
        return ret;
    }

    public static List<Channel> matchByName(Pattern pattern,
                                            boolean all,
                                            Repository<Channel> channels,
                                            Pagination pagination) {
        var found = (Iterable<Channel>) null;
        if (all) {
            found = channels.findMatched("name", pattern, pagination);
        } else {
            found = channels.findMatchedWithFields(
                    "name",
                    pattern,
                    Map.of(
                            "privacy", Privacy.PUBLIC
                    ),
                    pagination
            );
        }
        var ret = new LinkedList<Channel>();
        found.forEach(ret::add);
        return ret;
    }

    private static void delete(Iterable<Post> ids, Repository<Post> posts, Repository<Comment> comments) {
        var values = new LinkedList<String>();
        ids.forEach(e -> values.add(e.getId()));
        posts.deleteAll(values);
        comments.deleteAll("postId", values);
    }

    public static boolean delete(String channelId,
                                 Repository<Channel> channels,
                                 Repository<Post> posts,
                                 Repository<Comment> comments) {
        if (!channels.delete(channelId)) {
            return false;
        }
        delete(
                posts.findByField("channelId", channelId, List.of("id")),
                posts,
                comments
        );
        return true;
    }

    public static boolean delete(String userId,
                                 String channelId,
                                 Repository<Channel> channels,
                                 Repository<Post> posts,
                                 Repository<Comment> comments) {
        var fields = Map.<String, Object>of(
                "id", channelId,
                "ownerId", userId
        );
        if (!channels.delete(fields)) {
            return false;
        }
        delete(
                posts.findByField("channelId", channelId, List.of("id")),
                posts,
                comments
        );
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public List<Post> retrievePosts(Repository<Post> posts, Pagination pagination) {
        var found = posts.findByField("channelId", id, pagination);
        var ret = new LinkedList<Post>();
        found.forEach(ret::add);
        return ret;
    }

    public List<Post> findPostsByTitle(String title, Repository<Post> posts, Pagination pagination) {
        var fields = Map.<String, Object>of(
                "channelId", id,
                "title", title
        );
        var found = posts.findByField(fields, pagination);
        var ret = new LinkedList<Post>();
        found.forEach(ret::add);
        return ret;
    }

    public List<Post> matchPostsByTitle(Pattern pattern, Repository<Post> posts, Pagination pagination) {
        var fields = Map.<String, Object>of("channelId", id);
        var found = posts.findMatchedWithFields("title", pattern, fields, pagination);
        var ret = new LinkedList<Post>();
        found.forEach(ret::add);
        return ret;
    }
}
