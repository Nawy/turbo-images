package com.turbo.controller;

import com.turbo.model.Post;
import com.turbo.model.SecurityRole;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.model.user.User;
import com.turbo.service.AuthorizationService;
import com.turbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by rakhmetov on 24.05.17.
 */
@RestController
public class UserController {

    private final AuthorizationService authorizationService;
    private final UserService userService;

    @Autowired
    public UserController(AuthorizationService authorizationService, UserService userService) {
        this.authorizationService = authorizationService;
        this.userService = userService;
    }

    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/info")
    public User getCurrentUser() {
        return authorizationService.getCurrentUser();
    }

    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/posts")
    public List<Post> getUserPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        if (size <= 0 || page < 0) throw new BadRequestHttpException("page and size can't be negative");
        //FIXME NOTHING HERE
        return null;
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/update/user/info")
    public User updateUserInfo(@RequestBody User user) {
        if (user.getId() == null) throw new BadRequestHttpException("No id was found!");
        return userService.update(user);
    }

}
