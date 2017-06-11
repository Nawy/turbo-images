package com.turbo.controller;

import com.turbo.model.Post;
import com.turbo.model.search.SearchSort;
import com.turbo.model.SecurityRole;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.model.page.Paginator;
import com.turbo.model.User;
import com.turbo.service.AuthorizationService;
import com.turbo.service.PostService;
import com.turbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rakhmetov on 24.05.17.
 */
@RestController
public class UserController {

    private final AuthorizationService authorizationService;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public UserController(AuthorizationService authorizationService, UserService userService, PostService postService) {
        this.authorizationService = authorizationService;
        this.userService = userService;
        this.postService = postService;
    }

    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/info")
    public User getCurrentUser() {
        return authorizationService.getCurrentUser();
    }

    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/posts")
    public Paginator<Post> getUserPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "RATING") SearchSort sort
    ) {
        User user = authorizationService.getCurrentUser();
        return postService.getUserPosts(page, user.getId(), sort);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/update/user/info")
    public User updateUserInfo(@RequestBody User user) {
        if (user.getId() == null) throw new BadRequestHttpException("No id was found!");
        return userService.update(user);
    }
}
