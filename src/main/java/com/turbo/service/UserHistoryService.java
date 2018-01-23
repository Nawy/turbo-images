package com.turbo.service;

import com.turbo.model.UserHistory;
import com.turbo.repository.aerospike.user.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserHistoryService {

    private final UserHistoryRepository userStatsRepository;

    private UserHistory get(long userId) {
        UserHistory userHistory = userStatsRepository.get(userId);
        return userHistory == null ? new UserHistory(userId) : userHistory;
    }

    public void setViews(long userId, long postId) {
        UserHistory userHistory = get(userId);
        userHistory.setPostIsViewed(postId);
        userStatsRepository.save(userHistory);
    }

    public void setLikes(long userId, long postId, boolean haveLike) {
        UserHistory userHistory = get(userId);
        userHistory.setPostHaveLike(postId, haveLike);
        userStatsRepository.save(userHistory);
    }

    // if boolean is null then no rating was set, if true, then uprate, else downrate
    public void setRatingChange(long userId, long postId, Boolean ratingIsUprated) {
        UserHistory userHistory = get(userId);
        userHistory.setPostRatingStatus(postId, ratingIsUprated);
        userStatsRepository.save(userHistory);
    }

    // if boolean is null then no rating was set, if true, then uprate, else downrate
    public void setCommentRatingChange(long userId, long postId, long commentId, Boolean ratingIsUprated) {
        UserHistory userHistory = get(userId);
        userHistory.setCommentRatingStatus(postId, commentId, ratingIsUprated);
        userStatsRepository.save(userHistory);
    }

    // if null not viewed at all
    public LocalDateTime whenWasViewed(long userId, long postId) {
        UserHistory userHistory = get(userId);
        return userHistory.whenPostsWhereViewed(postId);
    }

    public boolean isPostHasLike(long userId, long postId) {
        UserHistory userHistory = get(userId);
        return userHistory.isPostHaveLike(postId);
    }

    // if boolean is null then no rating was set, if true, then uprate, else downrate
    public Boolean getPostRatingChange(long userId, long postId) {
        UserHistory userHistory = get(userId);
        return userHistory.getPostRatingStatus(postId);
    }

    // if boolean is null then no rating was set, if true, then uprate, else downrate
    public Boolean getCommentRatingChange(long userId, long postId, long commentId) {
        UserHistory userHistory = get(userId);
        return userHistory.getCommentRatingStatus(postId, commentId);
    }

}
