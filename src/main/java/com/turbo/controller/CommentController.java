package com.turbo.controller;

import com.turbo.model.SecurityRole;
import com.turbo.model.dto.CommentModificationDTO;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.service.CommentService;
import com.turbo.util.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save/comment")
    public void saveComment(@RequestBody CommentModificationDTO commentModificationDTO) {
        Long postId = commentModificationDTO.getDecodedPostId();
        if (postId == null) throw new BadRequestHttpException("empty post id");
        commentService.addComment(postId, commentModificationDTO.toRepoModel());
    }

    @Secured(SecurityRole.USER)
    @DeleteMapping("/delete/comment")
    public void deleteComment(@RequestParam("comment_id") String commentId, @RequestParam("post_id") String postId) {
        commentService.deleteComment(
                EncryptionService.decodeHashId(postId),
                EncryptionService.decodeHashId(commentId)
        );
    }

    @PostMapping("/change/comment/rating")
    public void changeCommentRating(
            @RequestParam("comment_id") String commentId,
            @RequestParam("post_id") String postId,
            @RequestParam("rating") long rating
    ) {
        commentService.changeCommentRating(
                EncryptionService.decodeHashId(postId),
                EncryptionService.decodeHashId(commentId),
                rating
        );
    }
}
