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
    private Map<Long, Map<Long, Boolean>> commentRatings; // Map<(id of post), Map<(id of comment),(bool = true rating is raised)>

    public UserHistory(long userId) {
        this.userId = userId;
        posts = Collections.emptyMap();
        commentRatings = Collections.emptyMap();
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

    // if null then no rating was set, if true, then uprate, else downrate
    public void setPostRatingStatus(long postId, Boolean upvote) {
        UserPostInfo userPostInfo = getUserPostsInfo(postId);
        userPostInfo.setUprated(upvote);
    }

    public Boolean getPostRatingStatus(long postId) {
        UserPostInfo userPostInfo = getUserPostsInfo(postId);
        return userPostInfo.getUprated();
    }

    // if boolean is null then no rating was set, if true, then uprate, else downrate
    public void setCommentRatingStatus(long postId, long commentId, Boolean upvote) {
        Map<Long, Boolean> postCommentsRatingStatus = commentRatings.getOrDefault(postId, new HashMap<>());
        postCommentsRatingStatus.put(commentId, upvote);
        commentRatings.put(postId, postCommentsRatingStatus);
    }

    // if boolean is null then no rating was set, if true, then uprate, else downrate
    public Boolean getCommentRatingStatus(long postId, long commentId) {
        Map<Long, Boolean> postCommentsRatingStatus = commentRatings.get(postId);
        if (postCommentsRatingStatus == null) return null;
        return postCommentsRatingStatus.get(commentId);
    }

    @Data
    private static class UserPostInfo implements Serializable {

        private Long postId;
        private boolean haveLike; // default false
        private LocalDateTime viewed; // if null, post was not viewed
        private Boolean uprated; // if null then no rating was set, if true, then uprate, else downrate

        UserPostInfo(long postId) {
            this.postId = postId;
        }
    }
}
