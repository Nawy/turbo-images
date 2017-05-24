package com.turbo.controller;

import com.turbo.model.Post;
import com.turbo.model.SecurityRole;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.model.user.User;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by rakhmetov on 24.05.17.
 */
@RestController
public class UserController {

    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/info")
    public User getCurrentUser(){
        //FIXME
        return null;
    }

    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/posts")
    public List<Post> getUserPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ){
        if (size <= 0 || page < 0) throw new BadRequestHttpException("page and size can't be negative");
        //FIXME NOTHING HERE
        return null;
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/update/user/info")
    public User updateUserInfo(@RequestBody User user){
        return null;
    }

}
