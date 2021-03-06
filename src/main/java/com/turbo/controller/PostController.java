package com.turbo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.Post;
import com.turbo.model.SecurityRole;
import com.turbo.model.aerospike.PostRepoModel;
import com.turbo.model.dto.*;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.model.page.Page;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.SearchPattern;
import com.turbo.model.search.SearchPeriod;
import com.turbo.model.search.SearchSort;
import com.turbo.service.AuthorizationService;
import com.turbo.service.PostService;
import com.turbo.service.UserHistoryService;
import com.turbo.util.EncryptionService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rakhmetov on 09.05.17.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final UserHistoryService userHistoryService;
    private final AuthorizationService authorizationService;


    @GetMapping("/post")
    public List<PostPreviewDto> getPost(
            @RequestParam("query") String query,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return toPostSearchDtos(
                postService.getPost(query, new Page(page))
        );
    }

    //FIXME NOT WORKING!!! STAT IS BAD!!!
    @GetMapping("/get/viral/post")
    public List<PostPreviewDto> getMostViral(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "RATING") SearchSort searchSort,
            @RequestParam(value = "period", defaultValue = "DAY") SearchPeriod searchPeriod
    ) {
        return toPostSearchDtos(
                postService.getMostViral(page, searchPeriod, searchSort)
        );
    }

    //FIXME NOT WORKING???!!!
    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/posts")
    public List<PostPreviewDto> getUserPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "DATE") SearchSort sort,
            @RequestParam(value = "period", defaultValue = "ALL_TIME") SearchPeriod period,
            @RequestParam(value = "order", defaultValue = "DESC") SearchOrder order
    ) {
        final long userId = authorizationService.getCurrentUserId();
        return toPostSearchDtos(
                postService.getUserPosts(page, userId, new SearchPattern(period, sort, order))
        );
    }

    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/posts/by_date")
    public List<PostPreviewDto> getUserPostsByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS") LocalDateTime startDate
    ) {
        final long userId = authorizationService.getCurrentUserId();
        return toPostSearchDtos(
                postService.getUserPostsByDate(userId, startDate)
        );
    }

    @GetMapping("/get/posts/by_date")
    public List<PostPreviewDto> getPostsByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS") LocalDateTime startDate
    ) {
        List<Post> posts = postService.getPostsByDate(startDate);
        return toPostSearchDtos(posts);
    }

    private List<PostPreviewDto> toPostSearchDtos(List<Post> posts) {
        return posts.stream()
                .map(PostPreviewDto::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/get/post/{id}")
    public PostDto get(@PathVariable("id") String id) {
        if (StringUtils.isBlank(id)) throw new BadRequestHttpException("id is blank");
        Post post = postService.getPostById(EncryptionService.decodeHashId(id));
        return PostDto.from(post);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/save/post")
    public PostDto save(@RequestBody TransferPost post) {
        final long currentUserId = authorizationService.getCurrentUserId();
        PostRepoModel postRepoModel = post.toPostRepoModel(currentUserId);
        return PostDto.from(
                postService.save(postRepoModel)
        );
    }

    @Secured(SecurityRole.USER)
    @DeleteMapping("/delete/post/{id}")
    public void deletePost(@PathVariable("id") String id) {
        postService.deletePost(
                EncryptionService.decodeHashId(id)
        );
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/post/content")
    public PostDto updatePost(@RequestBody PostContentDto postContentDto) {
        Post updatePost = postService.updatePostContent(postContentDto);
        return PostDto.from(updatePost);
    }

    @PostMapping("/post/views")
    public PostDto updatePostView(@RequestBody PostRatingDto postRatingDto) {
        Post updatePost = postService.updatePostRating(
                PostRatingDto.builder()
                        .id(postRatingDto.getId())
                        .views(1L)
                        .build()
        );
        return PostDto.from(updatePost);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/post/rating")
    public PostDto updatePostRating(@RequestBody PostRatingDto postRatingDto) {
        final long userId = authorizationService.getCurrentUserId();
        final boolean isPostHasLike = userHistoryService.isPostHasLike(userId, postRatingDto.getId());

        if(isPostHasLike && postRatingDto.getRating() != 0) {
            throw new BadRequestHttpException("Post already has your mark");
        }

        Post updatePost = postService.updatePostRating(postRatingDto);
        userHistoryService.setRatingChange(userId,postRatingDto.getId(), true);
        return PostDto.from(updatePost);
    }


    private static class PostMetaDto {
        private long postId;
        private String field;

        public PostMetaDto(
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
