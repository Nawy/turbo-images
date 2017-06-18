package com.turbo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.SecurityRole;
import com.turbo.model.Session;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.model.User;
import com.turbo.service.AuthorizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ermolaev on 5/7/17.
 */
@RestController
public class AuthorisationController {

    private final AuthorizationService authorizationService;

    @Autowired
    public AuthorisationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/signin")
    public void signin(@RequestBody UserCredentialsDto userCredentialsDto) {
        if (StringUtils.isBlank(userCredentialsDto.getEmail())
                || StringUtils.isBlank(userCredentialsDto.getPassword())) {
            throw new BadRequestHttpException("Bad credentials");
        }
        authorizationService.login(
                userCredentialsDto.getEmail(),
                userCredentialsDto.getPassword()
        );
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/logout")
    public void logout() {
        authorizationService.logout();
    }

    @PostMapping("/signup")
    public Session signup(@RequestBody User user) {
        return authorizationService.signup(user);
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
