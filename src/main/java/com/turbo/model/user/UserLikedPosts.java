package com.turbo.model.user;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.List;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Table("liked_posts")
public class UserLikedPosts {

    @PrimaryKey
    private long userId;
    private List<Long> likedPostIds; // some other posts what liked by user

    public UserLikedPosts(long userId, List<Long> likedPostIds) {
        this.userId = userId;
        this.likedPostIds = likedPostIds;
    }

    public long getUserId() {
        return userId;
    }

    public List<Long> getLikedPostIds() {
        return likedPostIds;
    }
}
