package com.turbo.controller;

import com.turbo.model.comment.Comment;
import com.turbo.model.comment.CommentReplyType;
import com.turbo.model.dto.CommentDto;
import com.turbo.model.dto.CommentModificationDTO;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.SearchSort;
import com.turbo.model.search.field.CommentField;
import com.turbo.model.search.field.CommentFieldNames;
import com.turbo.service.CommentService;
import com.turbo.util.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/get/post/comments")
    public List<CommentDto> getPostComments(
            @RequestParam("reply_id") String encodedReplyId,
            @RequestParam(value = "size", defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", defaultValue = "DATE") SearchSort sort,
            @RequestParam(value = "order", defaultValue = "DESC") SearchOrder searchOrder
    ) {
        if (sort == SearchSort.VIEWS) throw new BadRequestHttpException("views sort unsupported");
        long replyId = EncryptionService.decodeHashId(encodedReplyId);
        return CommentDto.from(
                commentService.getByReply(replyId, CommentReplyType.POST, pageSize, sort, searchOrder)
        );
    }

    @GetMapping("/get/comment/comments")
    public List<CommentDto> getCommentComments(
            @RequestParam("reply_id") String encodedReplyId,
            @RequestParam(value = "size", defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", defaultValue = "DATE") SearchSort sort,
            @RequestParam(value = "order", defaultValue = "DESC") SearchOrder searchOrder
    ) {
        if (sort == SearchSort.VIEWS) throw new BadRequestHttpException("views sort unsupported");
        long replyId = EncryptionService.decodeHashId(encodedReplyId);
        return CommentDto.from(
                commentService.getByReply(replyId, CommentReplyType.COMMENT, pageSize, sort, searchOrder)
        );
    }

    @PostMapping("/save/comment")
    public CommentDto save(@RequestBody CommentModificationDTO commentModificationDTO) {
        Comment comment = commentService.save(
                commentModificationDTO.toRepoModel()
        );

        return CommentDto.from(comment);
    }

    @GetMapping("/get/comment/{id}")
    public CommentDto get(@PathVariable("id") String encodedId) {
        long id = EncryptionService.decodeHashId(encodedId);
        return CommentDto.from(
                commentService.get(id)
        );
    }

    @DeleteMapping("/delete/comment/{id}")
    public void delete(@PathVariable("id")String commentId){
        commentService.delete(
                EncryptionService.decodeHashId(commentId)
        );
    }
}
