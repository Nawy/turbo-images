package com.turbo.controller;

import com.turbo.model.Post;
import com.turbo.model.SearchSort;
import com.turbo.model.exception.http.BadRequestHttpException;
import com.turbo.model.page.Paginator;
import com.turbo.service.HashIdService;
import com.turbo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rakhmetov on 09.05.17.
 */
@RestController
public class PostController {

    private final PostService postService;
    private final HashIdService hashIdService;

    @Autowired
    public PostController(PostService postService, HashIdService hashIdService) {
        this.postService = postService;
        this.hashIdService = hashIdService;
    }

    @GetMapping("/get/viral/post")
    public Paginator<Post> getMostViral(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "RATING") SearchSort searchSort
    ) {
        return postService.getMostViral(page, searchSort);
    }

    @GetMapping("/get/post/{id}")
    public Post get(@PathVariable("id") String id) {
        return postService.getPostById(
                hashIdService.decodeHashId(id)
        );
    }

    @PostMapping("/add/post")
    public Post add(@RequestBody Post post) {
        return postService.addPost(post);
    }

    @PostMapping("/update/post")
    public Post update(@RequestBody Post post) {
        if (post.getId() == null) throw new BadRequestHttpException("id can't be null");
        return postService.update(post);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") String id) {
        postService.delete(
                hashIdService.decodeHashId(id)
        );
    }

}
