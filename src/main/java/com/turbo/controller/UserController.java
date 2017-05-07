package com.turbo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ermolaev on 5/7/17.
 */
@RestController
public class UserController {

    @PostMapping("/login")
    public void login() {
    }

}
