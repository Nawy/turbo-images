package com.turbo.model;

import lombok.Data;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserHistory implements Serializable, IdHolder {

    private Long userId;
    private Map<Long, UserPostInfo> posts; // id of posts where something happen to them

    public UserHistory(long userId) {
        this.userId = userId;
        posts = Collections.emptyMap();
    }

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public void setId(Long id) {
        throw new NotImplementedException();
    }

    private UserPostInfo getUserPostsInfo(long postId) {
        return posts.getOrDefault(postId, new UserPostInfo(postId));
    }

    public void setPostIsViewed(long postId) {
        UserPostInfo userPostInfo = getUserPostsInfo(postId);
        userPostInfo.setViewed(LocalDateTime.now());
    }

    public LocalDateTime whenPostsWhereViewed(long postId) {
        UserPostInfo userPostInfo = getUserPostsInfo(postId);
        return userPostInfo.getViewed();
    }

    public void setPostHaveLike(long postId, boolean haveLike) {
        UserPostInfo userPostInfo = getUserPostsInfo(postId);
        userPostInfo.setHaveLike(haveLike);
    }

    public boolean isPostHaveLike(long postId) {
        UserPostInfo userPostInfo = getUserPostsInfo(postId);
        return userPostInfo.isHaveLike();
    }

    @Data
    private static class UserPostInfo implements Serializable {

        private Long postId;
        private boolean haveLike; // default false
        private LocalDateTime viewed; // if null, post was not viewed

        UserPostInfo(long postId) {
            this.postId = postId;
        }
    }
}
