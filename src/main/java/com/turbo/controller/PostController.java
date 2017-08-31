package com.turbo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.Post;
import com.turbo.model.SecurityRole;
import com.turbo.model.dto.PostDto;
import com.turbo.model.dto.PostSearchDto;
import com.turbo.model.page.Paginator;
import com.turbo.model.search.SearchPeriod;
import com.turbo.model.search.SearchSort;
import com.turbo.service.PostService;
import com.turbo.util.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Created by rakhmetov on 09.05.17.
 */
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/get/viral/post")
    public Paginator<PostSearchDto> getMostViral(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "RATING") SearchSort searchSort,
            @RequestParam(value = "period", defaultValue = "DAY") SearchPeriod searchPeriod
    ) {
        return new Paginator<>(
                page,
                postService.getMostViral(page, searchPeriod, searchSort)
                        .stream()
                        .map(PostSearchDto::from)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/get/post/{id}")
    public PostDto get(@PathVariable("id") String id) {
        return PostDto.from(
                postService.getPostById(EncryptionService.decodeHashId(id))
        );
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/save/post")
    public PostDto save(@RequestBody Post post) {
        return PostDto.from(
                postService.save(post)
        );
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/edit/post/name")
    public PostDto editPostName(@RequestBody PostEditDto postEditDto) {
       Post updatePost = postService.updatePostName(
                postEditDto.getPostId(),
                postEditDto.getField()
        );
       return PostDto.from(updatePost);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/edit/post/description")
    public PostDto editPostDescription(@RequestBody PostEditDto postEditDto) {
        Post updatePost = postService.updatePostDescription(
                postEditDto.getPostId(),
                postEditDto.getField()
        );
        return PostDto.from(updatePost);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/add/post/tag")
    public PostDto addPostTag(@RequestBody PostEditDto postEditDto) {
        Post updatePost = postService.addPostTag(
                postEditDto.getPostId(),
                postEditDto.getField()
        );
        return PostDto.from(updatePost);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/remove/post/tag")
    public PostDto removePostTag(@RequestBody PostEditDto postEditDto) {
        Post updatePost = postService.removePostTag(
                postEditDto.getPostId(),
                postEditDto.getField()
        );
        return PostDto.from(updatePost);
    }

    @Secured(SecurityRole.USER)
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") String id) {
        postService.delete(
                EncryptionService.decodeHashId(id)
        );
    }

    private static class PostEditDto {
        private long postId;
        private String field;

        public PostEditDto(
                @JsonProperty(value = "post_id", required = true) long postId,
                @JsonProperty(value = "field", required = true) String field
        ) {
            this.postId = postId;
            this.field = field;
        }

        public long getPostId() {
            return postId;
        }

        public String getField() {
            return field;
        }
    }

}
