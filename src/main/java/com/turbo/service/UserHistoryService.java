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

    // if null not viewed at all
    public LocalDateTime whenWasViewed(long userId, long postId) {
        UserHistory userHistory = get(userId);
        return userHistory.whenPostsWhereViewed(postId);
    }

    public boolean isPostHasLike(long userId, long postId) {
        UserHistory userHistory = get(userId);
        return userHistory.isPostHaveLike(postId);
    }
}
