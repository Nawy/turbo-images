package com.turbo.model.search.stat;

/**
 * Created by ermolaev on 6/4/17.
 */
public class PostViewEntity {

    private long id;
    private long views;

    public PostViewEntity(long id, long views) {
        this.id = id;
        this.views = views;
    }

    public long getId() {
        return id;
    }

    public long getViews() {
        return views;
    }
}
