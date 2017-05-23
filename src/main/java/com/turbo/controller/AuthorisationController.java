package com.turbo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.Session;
import com.turbo.model.user.User;
import com.turbo.service.AuthorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ermolaev on 5/7/17.
 */
@RestController
public class AuthorisationController {

    private final AuthorisationService authorisationService;

    @Autowired
    public AuthorisationController(AuthorisationService authorisationService) {
        this.authorisationService = authorisationService;
    }

    @PostMapping("/signin")
    public void signin(@RequestBody UserCredentialsDto userCredentialsDto) {
        authorisationService.signin(
                userCredentialsDto.getEmail(),
                userCredentialsDto.getPassword()
        );
    }

    @PostMapping("/signup")
    public Session signup(@RequestBody User user) {
        return authorisationService.signup(user);
    }

    private static class UserCredentialsDto {
        private String email;
        private String password;

        public UserCredentialsDto(
                @JsonProperty(value = "email", required = true) String email,
                @JsonProperty(value = "password", required = true) String password
        ) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
