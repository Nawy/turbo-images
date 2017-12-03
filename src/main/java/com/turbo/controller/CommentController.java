package com.turbo.controller;

import com.turbo.model.comment.Comment;
import com.turbo.model.dto.CommentDto;
import com.turbo.model.dto.CommentModificationDTO;
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

    //TODO remove it's just for test
    @GetMapping("/get/search/comments")
    public List<CommentDto> getAllPostComments() {
        return CommentDto.from(
                commentService.getFromSearchForTest()
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
}
