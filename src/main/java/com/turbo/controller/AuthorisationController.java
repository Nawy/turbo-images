package com.turbo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.*;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.repository.util.EncryptionService;
import com.turbo.repository.util.Headers;
import com.turbo.service.AuthorizationService;
import com.turbo.service.SessionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * Created by ermolaev on 5/7/17.
 */
@RestController
@CrossOrigin
public class AuthorisationController {

    private final AuthorizationService authorizationService;
    private final SessionService sessionService;

    @Autowired
    public AuthorisationController(AuthorizationService authorizationService, SessionService sessionService) {
        this.authorizationService = authorizationService;
        this.sessionService = sessionService;
    }

    @PostMapping("/signin")
    public void signin(
            @RequestBody UserCredentialsDto userCredentialsDto,
            @RequestHeader(value = Headers.DEVICE_TYPE, required = false) DeviceType deviceType,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (StringUtils.isBlank(userCredentialsDto.getEmail()) || StringUtils.isBlank(userCredentialsDto.getPassword())) {
            throw new BadRequestHttpException("Bad credentials");
        }
        Session session = authorizationService.login(
                userCredentialsDto.getEmail(),
                userCredentialsDto.getPassword(),
                deviceType,
                request.getRemoteAddr()
        );
        addSessionCookieToResponse(response, session.getUserId());
    }

    private void addSessionCookieToResponse(HttpServletResponse response, long sessionId) {
        Cookie cookie = createCookie(sessionId);
        response.addCookie(cookie);
    }

    private Cookie createCookie(long sessionId) {
        String encryptedSessionId = EncryptionService.encodeHashId(sessionId);
        Cookie cookie = new Cookie(SecurityHeader.SESSION_COOKIE_NAME, encryptedSessionId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(sessionService.getSessionMaxAge());
        return cookie;
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        long sessionId = authorizationService.logout();
        Cookie cookie = createCookie(sessionId);
        cookie.setMaxAge(0); // age = 0 make cookie self delete
        response.addCookie(cookie);
    }

    @PostMapping("/signup")
    public Session signup(
            @RequestBody UserSignupDto userDto,
            @RequestHeader(value = Headers.DEVICE_TYPE, required = false) DeviceType deviceType,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (!userDto.isValidCredentials()) throw new BadRequestHttpException("None of fields can be blank");
        User user = new User(
                null,
                userDto.getName(),
                null,
                userDto.getEmail(),
                userDto.getPassword(),
                LocalDateTime.now()
        );
        Session session = authorizationService.signup(user, deviceType, request.getRemoteAddr());
        addSessionCookieToResponse(response, session.getUserId());
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

        public boolean isValidCredentials() {
            return StringUtils.isNotBlank(name) &&
                    StringUtils.isNotBlank(email) &&
                    StringUtils.isNotBlank(password);
        }
    }
}
