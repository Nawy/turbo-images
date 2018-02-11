package com.turbo.controller;

import com.turbo.model.Post;
import com.turbo.model.RatingStatus;
import com.turbo.model.SecurityRole;
import com.turbo.model.aerospike.PostRepoModel;
import com.turbo.model.dto.PostContentDto;
import com.turbo.model.dto.PostDto;
import com.turbo.model.dto.PostPreviewDto;
import com.turbo.model.dto.TransferPost;
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

    @Secured(SecurityRole.USER)
    @PostMapping("/post/add/views")
    public PostDto updatePostView(@RequestParam("post_id") String postId) {
        Post updatePost = postService.addPostViews(
                EncryptionService.decodeHashId(postId)
        );
        return PostDto.from(updatePost);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/post/rating")
    public PostDto updatePostRating(
            @RequestParam("post_id") String postId,
            @RequestParam("rating_status") RatingStatus ratingStatus
    ) {
        Post updatePost = postService.updatePostRating(
                EncryptionService.decodeHashId(postId),
                authorizationService.getCurrentUserId(),
                ratingStatus
        );
        return PostDto.from(updatePost);
    }
}
