package com.turbo.service;

import com.turbo.model.Rating;
import com.turbo.model.aerospike.CommentRepoModel;
import com.turbo.model.aerospike.PostRepoModel;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.turbo.util.IdGenerator.ITERATIONS_TO_GENERATE_ID;

@Log
@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostService postService;
    private final UserHistoryService userHistoryService;

    public void addComment(long postId, CommentRepoModel commentRepoModel) {
        PostRepoModel post = postService.getRawPost(postId);
        Long commentId = generateCommentId(post.getComments());
        commentRepoModel.setId(commentId);
        Map<Long,CommentRepoModel> newComments = new HashMap<>(post.getComments());
        newComments.put(commentId,commentRepoModel);
        postService.saveRawPost(post);
    }

    public void deleteComment(long postId, long commentId) {
        PostRepoModel post = postService.getRawPost(postId);
        Map<Long,CommentRepoModel> newComments = new HashMap<>(post.getComments());
        CommentRepoModel deletedComment = newComments.remove(commentId);
        if (deletedComment == null) throw new NotFoundHttpException("No such comment found");
        post.setComments(newComments);
        postService.saveRawPost(post);
    }

    public void changeCommentRating(long userId, long postId, long commentId, Boolean rating) {
        PostRepoModel post = postService.getRawPost(postId);
        CommentRepoModel comment = post.getComments().get(commentId);
        Rating commentRating = comment.getRating();
        if (commentRating == null) {
            if (rating == null) return; // comment don't have rating, nothing reset
            Rating newCommentRating = new Rating();
            newCommentRating.changeRating(rating);
            comment.setRating(newCommentRating);
        } else {

            if (rating == null) {
                Boolean ratingHistory = userHistoryService.getCommentRatingChange(userId, postId, commentId);
                if (ratingHistory == null) return; // rating should not be reset
                commentRating.resetOneRating(ratingHistory);
            } else {
                Boolean ratingHistory = userHistoryService.getCommentRatingChange(userId, postId, commentId);
                if (ratingHistory != null){
                    commentRating.resetOneRating(ratingHistory);
                }
                commentRating.changeRating(rating);
            }
        }

        postService.saveRawPost(post);
        userHistoryService.setCommentRatingChange(userId, postId, commentId, rating);
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
}
