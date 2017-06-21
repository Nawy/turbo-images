package com.turbo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.SecurityHeader;
import com.turbo.model.SecurityRole;
import com.turbo.model.Session;
import com.turbo.model.User;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.service.AuthorizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

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
    public void signin(@RequestBody UserCredentialsDto userCredentialsDto, HttpServletResponse response) {
        if (StringUtils.isBlank(userCredentialsDto.getEmail())
                || StringUtils.isBlank(userCredentialsDto.getPassword())) {
            throw new BadRequestHttpException("Bad credentials");
        }
        Session session = authorizationService.login(
                userCredentialsDto.getEmail(),
                userCredentialsDto.getPassword()
        );
        addSessionCookieToResponse(response,session.getId());
    }

    private void addSessionCookieToResponse(HttpServletResponse response, long sessionId) {
        Cookie cookie = createCookie(sessionId);
        response.addCookie(cookie);
    }

    private Cookie createCookie(long sessionId) {
        Cookie cookie = new Cookie(SecurityHeader.SESSION_COOKIE_NAME, Long.toString(sessionId));
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        authorizationService.logout();
        Cookie cookie = createCookie(0);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @PostMapping("/signup")
    public Session signup(@RequestBody UserSignupDto userDto, HttpServletRequest request, HttpServletResponse response) {
        User user = new User(
                userDto.getName(),
                null,
                userDto.getEmail(),
                userDto.getPassword(),
                LocalDateTime.now(),
                request.getRemoteAddr()
        );
        Session session = authorizationService.signup(user);
        addSessionCookieToResponse(response,session.getId());
        return session;
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


    private static class UserSignupDto {
        private String name;
        private String email;
        private String password;

        public UserSignupDto(
                @JsonProperty(value = "name", required = true) String name,
                @JsonProperty(value = "email", required = true) String email,
                @JsonProperty(value = "password", required = true) String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
