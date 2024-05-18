package com.github.romanqed.devspark.models;

import com.github.romanqed.devspark.database.Model;
import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;

import java.util.*;

import static com.github.romanqed.devspark.CollectionUtil.asList;

@Model("feeds")
public final class Feed extends Owned implements Visible {
    private String id;
    private String name;
    private Privacy privacy;
    private Set<String> channelIds;
    private Set<String> tagIds;
    private Date created;
    private Date updated;

    public static boolean delete(Repository<Feed> feeds, String userId, String feedId) {
        var fields = Map.<String, Object>of(
                "_id", feedId,
                "ownerId", userId
        );
        return feeds.delete(fields);
    }

    public static Feed of(String owner, String name) {
        var ret = new Feed();
        ret.id = UUID.randomUUID().toString();
        ret.name = Objects.requireNonNull(name);
        ret.ownerId = Objects.requireNonNull(owner);
        ret.privacy = Privacy.PRIVATE;
        ret.channelIds = new HashSet<>();
        ret.tagIds = new HashSet<>();
        var now = new Date();
        ret.created = now;
        ret.updated = now;
        return ret;
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

    public Set<String> getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(Set<String> channelIds) {
        this.channelIds = channelIds;
    }

    public Set<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<String> tagIds) {
        this.tagIds = tagIds;
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
        return privacy == Privacy.PUBLIC;
    }

    @SuppressWarnings("unchecked")
    public List<Post> getPosts(Repository<Post> posts, Pagination pagination) {
        /*
        Heuristic:
        1) If feed has associated public channels, select public posts with the specified tags from it
        2) If feed has associated public channels, but has no tags, then just get posts from channels
        3) Otherwise, just select public posts with the specified tags
        4) If feed has no tags, then select all public posts
         */
        /*
        some tag from tags in post.tags && post.channelId in channels && post.privacy = PUBLIC
         */
        var ins = new HashMap<String, Iterable<?>>();
        ins.put("tagIds", this.tagIds);
        if (channelIds != null && !channelIds.isEmpty()) {
            ins.put("channelId", channelIds);
        }
        var map = (Map<String, Iterable<Object>>) (Map<?, ?>) ins;
        var found = posts.findByField(map, Map.of("privacy", Privacy.PUBLIC), pagination);
        return asList(found);
    }
}
