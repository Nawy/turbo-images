package com.turbo.controller;

import com.turbo.model.Comment;
import com.turbo.model.RatingStatus;
import com.turbo.model.SecurityRole;
import com.turbo.model.dto.AddCommentDTO;
import com.turbo.model.dto.CommentDto;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.service.AuthorizationService;
import com.turbo.service.CommentService;
import com.turbo.util.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final AuthorizationService authorizationService;
    private final CommentService commentService;

    @GetMapping("/get/comments/{postId}")
    public Map<String, CommentDto> getComments(@PathVariable("postId") String postId) {
        Map<Long,Comment> commentMap = commentService.getComments(
                EncryptionService.decodeHashId(postId)
        );

        return CommentDto.from(commentMap);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/add/comment")
    public void addComment(@RequestBody AddCommentDTO addCommentDTO) {
        Long postId = addCommentDTO.getDecodedPostId();
        if (postId == null) throw new BadRequestHttpException("empty post id");
        final long userId = authorizationService.getCurrentUserId();
        commentService.addComment(postId, addCommentDTO.toRepoModel(userId));
    }

    @Secured(SecurityRole.USER)
    @DeleteMapping("/delete/comment")
    public void deleteComment(@RequestParam("comment_id") String commentId, @RequestParam("post_id") String postId) {
        commentService.deleteComment(
                EncryptionService.decodeHashId(postId),
                EncryptionService.decodeHashId(commentId)
        );
    }

    @Secured(SecurityRole.USER)
    @DeleteMapping("/edit/comment")
    public void editComment(
            @RequestParam("comment_id") String commentId,
            @RequestParam("post_id") String postId,
            @RequestParam("content") String content
    ) {
        commentService.editComment(
                EncryptionService.decodeHashId(postId),
                EncryptionService.decodeHashId(commentId),
                content
        );
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/change/comment/rating")
    public void changeCommentRating(
            @RequestParam("comment_id") String commentId,
            @RequestParam("post_id") String postId,
            @RequestParam(value = "rating") RatingStatus rating
    ) {
        commentService.changeCommentRating(
                authorizationService.getCurrentUserId(),
                EncryptionService.decodeHashId(postId),
                EncryptionService.decodeHashId(commentId),
                rating
        );
    }
}
