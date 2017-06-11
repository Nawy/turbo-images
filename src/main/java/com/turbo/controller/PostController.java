package com.turbo.controller;

import com.turbo.model.Post;
import com.turbo.model.search.SearchSort;
import com.turbo.model.dto.PostDto;
import com.turbo.model.page.Paginator;
import com.turbo.model.search.SearchOrder;
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

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/get/viral/post")
    public Paginator<Post> getMostViral(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "RATING") SearchSort searchSort,
            @RequestParam(value = "sort_order", defaultValue = "DESC") SearchOrder searchOrder
    ) {
        return postService.getMostViral(page, searchSort, searchOrder);
    }

    @GetMapping("/get/post/{id}")
    public PostDto get(@PathVariable("id") String id) {
        return PostDto.from(
                postService.getPostById(HashIdService.decodeHashId(id))
        );
    }

    @PostMapping("/save/post")
    public PostDto save(@RequestBody Post post) {
        return PostDto.from(
                postService.save(post)
        );
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") String id) {
        postService.delete(
                HashIdService.decodeHashId(id)
        );
    }
}
