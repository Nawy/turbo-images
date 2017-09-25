package com.turbo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.Post;
import com.turbo.model.SecurityRole;
import com.turbo.model.dto.PostDto;
import com.turbo.model.dto.PostPreview;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.SearchPattern;
import com.turbo.model.search.SearchPeriod;
import com.turbo.model.search.SearchSort;
import com.turbo.service.AuthorizationService;
import com.turbo.service.PostService;
import com.turbo.util.EncryptionService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rakhmetov on 09.05.17.
 */
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final AuthorizationService authorizationService;

    public PostController(PostService postService, AuthorizationService authorizationService) {
        this.postService = postService;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/get/viral/post")
    public List<PostPreview> getMostViral(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "RATING") SearchSort searchSort,
            @RequestParam(value = "period", defaultValue = "DAY") SearchPeriod searchPeriod
    ) {
        return toPostSearchDtos(
                postService.getMostViral(page, searchPeriod, searchSort)
        );
    }

    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/posts")
    public List<PostPreview> getUserPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "NEWEST") SearchSort sort,
            @RequestParam(value = "period", defaultValue = "ALL_TIME") SearchPeriod period,
            @RequestParam(value = "order", defaultValue = "DESC") SearchOrder order
    ) {
        final long userId = authorizationService.getCurrentUserId();
        return toPostSearchDtos(
                postService.getUserPosts(page, userId, new SearchPattern(period, sort, order))
        );
    }

    private List<PostPreview> toPostSearchDtos(List<Post> posts) {
        return posts.stream()
                .map(PostPreview::from)
                .collect(Collectors.toList());
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
