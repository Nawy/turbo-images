package com.turbo.service;

import com.turbo.model.Comment;
import com.turbo.model.Rating;
import com.turbo.model.RatingStatus;
import com.turbo.model.User;
import com.turbo.model.aerospike.CommentRepoModel;
import com.turbo.model.aerospike.PostRepoModel;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.turbo.util.IdGenerator.ITERATIONS_TO_GENERATE_ID;

@Log
@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostService postService;
    private final UserService userService;

    public Map<Long, Comment> getComments(long postId) {
        PostRepoModel postRepoModel = postService.getRawPost(postId);
        //get Users
        Set<Long> userIds = postRepoModel.getComments().values().stream().map(CommentRepoModel::getUserId).collect(Collectors.toSet());
        List<User> users = userService.bulkGet(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        //make Comments
        return bulkMakeComments(postRepoModel, userMap);
    }

    public void addComment(long postId, CommentRepoModel commentRepoModel) {
        PostRepoModel post = postService.getRawPost(postId);
        Long commentId = generateCommentId(post.getComments());
        commentRepoModel.setId(commentId);
        Map<Long,CommentRepoModel> newComments = new HashMap<>(post.getComments());
        newComments.put(commentId,commentRepoModel);
        post.setComments(newComments);
        postService.saveRawPost(post);
    }

    public void deleteComment(long postId, long commentId) {
        editComment(
                postId,
                (comments) -> {
                    CommentRepoModel comment = comments.get(commentId);
                    nullCommentCheck(comment, postId, commentId);
                    comment.setDeleted(true);
                }
        );
    }

    public void editComment(long postId, long commentId, String content) {
        editComment(
                postId,
                (comments) -> {
                    CommentRepoModel comment = comments.get(commentId);
                    nullCommentCheck(comment, postId, commentId);
                    comment.setContent(content);
                }
        );
    }

    private void nullCommentCheck(CommentRepoModel comment, long postId, long commentId) {
        if (comment == null)
            throw new NotFoundHttpException(String.format("No comment found in post:%s with comment_id:%s", postId, commentId));
    }

    private void editComment(long postId, Consumer<Map<Long, CommentRepoModel>> consumerFunction) {
        PostRepoModel post = postService.getRawPost(postId);
        Map<Long, CommentRepoModel> newComments = new HashMap<>(post.getComments());
        consumerFunction.accept(newComments);
        post.setComments(newComments);
        postService.saveRawPost(post);
    }

    public void changeCommentRating(long userId, long postId, long commentId, RatingStatus ratingChange) {
        PostRepoModel post = postService.getRawPost(postId);
        CommentRepoModel comment = post.getComments().get(commentId);
        Map<Long, RatingStatus> newRatingHistory = new HashMap<>(comment.getRatingHistory());
        RatingStatus userRatingHistory = newRatingHistory.getOrDefault(userId, RatingStatus.EMPTY);

        //process rating
        Rating currentRating = comment.getRating();
        if (userRatingHistory == RatingStatus.EMPTY && ratingChange == RatingStatus.EMPTY) return; //nothing reset
        if (userRatingHistory != RatingStatus.EMPTY) {
            currentRating.resetOneRating(userRatingHistory == RatingStatus.UP);
            newRatingHistory.remove(userId);
        }
        if (ratingChange != RatingStatus.EMPTY) {
            currentRating.changeRating(ratingChange == RatingStatus.UP);
            newRatingHistory.put(userId, ratingChange);
        }

        comment.setRatingHistory(newRatingHistory);
        postService.saveRawPost(post);
    }

    private Long generateCommentId(Map<Long, CommentRepoModel> comments) {
        int iterations = 0;
        do {
            Long id = IdGenerator.generateRandomId();
            if (comments.get(id) == null) return id;
            iterations++;
            if (iterations > 5) log.warning("More than 5 iterations is needed, iteration count: " + iterations);
        } while (ITERATIONS_TO_GENERATE_ID > iterations);
        throw new InternalServerErrorHttpException("Can't save entity");
    }

    public Map<Long, Comment> bulkMakeComments(PostRepoModel postRepoModel, Map<Long, User> userMap) {
        return postRepoModel.getComments().values().stream()
                .map(commentRepoModel -> new Comment(commentRepoModel, userMap.get(commentRepoModel.getUserId())))
                .collect(Collectors.toMap(Comment::getId, Function.identity()));
    }


}
