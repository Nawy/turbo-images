package com.turbo.controller;

import com.turbo.model.Post;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.model.page.Page;
import com.turbo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get/post")
    public List<Post> getLastPost(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        if (size <= 0 || page < 0) throw new BadRequestHttpException("page and size can't be negative");
        return postService.getLastPosts(new Page(page, size));
    }

    @GetMapping("/get/post/{id}")
    public Post get(@PathVariable("id") String id) {
        return postService.getPostById(id);
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
        postService.delete(id);
    }

}
