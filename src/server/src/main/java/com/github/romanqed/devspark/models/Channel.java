package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;

import java.util.*;
import java.util.regex.Pattern;

import static com.github.romanqed.devspark.CollectionUtil.asList;

@Model("channels")
public final class Channel extends Owned implements Visible {
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

    public static boolean isVisible(Repository<Channel> channels, Collection<String> ids) {
        return channels.exists(ids, "privacy", Privacy.PUBLIC);
    }

    private static void delete(Repository<Post> posts, Repository<Comment> comments, Iterable<Post> ids) {
        var values = new LinkedList<String>();
        ids.forEach(e -> values.add(e.getId()));
        posts.deleteAll(values);
        comments.deleteAll("postId", values);
    }

    public static boolean delete(Repository<Channel> channels,
                                 Repository<Post> posts,
                                 Repository<Comment> comments,
                                 String channelId) {
        if (!channels.delete(channelId)) {
            return false;
        }
        delete(posts, comments, posts.findByField("channelId", channelId, List.of("_id")));
        return true;
    }

    public static boolean delete(Repository<Channel> channels,
                                 Repository<Post> posts,
                                 Repository<Comment> comments,
                                 String userId,
                                 String channelId) {
        var fields = Map.<String, Object>of(
                "_id", channelId,
                "ownerId", userId
        );
        if (!channels.delete(fields)) {
            return false;
        }
        delete(posts, comments, posts.findByField("channelId", channelId, List.of("_id")));
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

    @Override
    public boolean isVisible() {
        return this.privacy == Privacy.PUBLIC;
    }

    public List<Post> retrievePosts(Repository<Post> posts, Pagination pagination) {
        return asList(posts.findByField("channelId", id, pagination));
    }

    public List<Post> findPostsByTitle(Repository<Post> posts, String title, boolean all, Pagination pagination) {
        var fields = new HashMap<String, Object>();
        fields.put("channelId", id);
        fields.put("title", title);
        if (!all) {
            fields.put("privacy", Privacy.PUBLIC);
        }
        return asList(posts.findAnd(fields, pagination));
    }

    public List<Post> matchPostsByTitle(Repository<Post> posts, Pattern pattern, boolean all, Pagination pagination) {
        var fields = new HashMap<String, Object>();
        fields.put("channelId", id);
        if (!all) {
            fields.put("privacy", Privacy.PUBLIC);
        }
        return asList(posts.findMatchedWithFields("title", pattern, fields, pagination));
    }
}
