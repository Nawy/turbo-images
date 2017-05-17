package com.turbo.model.user;

import java.util.List;

/**
 * Created by rakhmetov on 09.05.17.
 */
public class UserUpvotedPosts {

    private long userId;
    private List<Long> upvotedPostsIds; // some other posts that upvoted by user

    public UserUpvotedPosts() {
    }

    public UserUpvotedPosts(long userId, List<Long> upvotedPostsIds) {
        this.userId = userId;
        this.upvotedPostsIds = upvotedPostsIds;
    }

    public long getUserId() {
        return userId;
    }

    public List<Long> getUpvotedPostsIds() {
        return upvotedPostsIds;
    }
}
