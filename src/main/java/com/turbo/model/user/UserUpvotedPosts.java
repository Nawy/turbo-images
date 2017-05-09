package com.turbo.model.user;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Table("upvoted_posts")
public class UserUpvotedPosts {

    @PrimaryKey
    private long userId;
    private long[] upvotedPostsIds; // some other posts that upvoted by user

    public UserUpvotedPosts(long userId, long[] upvotedPostsIds) {
        this.userId = userId;
        this.upvotedPostsIds = upvotedPostsIds;
    }

    public long getUserId() {
        return userId;
    }

    public long[] getUpvotedPostsIds() {
        return upvotedPostsIds;
    }
}
