package com.turbo.controller;

import com.turbo.model.Post;
import com.turbo.model.SecurityRole;
import com.turbo.model.User;
import com.turbo.model.page.Paginator;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.SearchPattern;
import com.turbo.model.search.SearchPeriod;
import com.turbo.model.search.SearchSort;
import com.turbo.service.AuthorizationService;
import com.turbo.service.PostService;
import com.turbo.service.UserService;
import org.luaj.vm2.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

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

    @GetMapping("/get/user/by_name_or_email")
    public User getUserByEmail(@RequestParam("name_or_email") String nameOrEmail) {
        return userService.get(nameOrEmail);
    }

    @GetMapping("/is/user/exists")
    public Map<String, Boolean> userExists(@RequestParam("name_or_email") String nameOrEmail) {
        return Collections.singletonMap(
                "exists",
                userService.isEmailOrNameExists(nameOrEmail)
        );
    }

    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/posts")
    public Paginator<Post> getUserPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "RATING") SearchSort sort,
            @RequestParam(value = "period", defaultValue = "ALL_TIME") SearchPeriod period,
            @RequestParam(value = "order", defaultValue = "DESC") SearchOrder order
    ) {
        User user = authorizationService.getCurrentUser();
        return new Paginator<>(
                page,
                postService.getUserPosts(page, user.getName(), new SearchPattern(period, sort, order))
        );
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/edit/user/info")
    public User updateUserInfo(@RequestBody User user) {
        return userService.update(user);
    }
}
