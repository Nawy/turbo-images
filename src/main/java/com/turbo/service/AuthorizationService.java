package com.turbo.service;

import com.turbo.model.Session;
import com.turbo.model.User;
import com.turbo.model.exception.ForbiddenHttpException;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.model.exception.UnauthorizedHttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


@Service
public class AuthorizationService {

    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public AuthorizationService(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    public Session login(String email, String password) {
        User user;
        try {
            user = userService.findByEmail(email);
        } catch (NotFoundHttpException e) {
            throw new ForbiddenHttpException("Wrong credentials!");
        }
        if (!user.getPassword().equals(password)) {
            throw new ForbiddenHttpException("Wrong credentials!");
        }
        return login(user.getName());
    }

    private Session login(String username) {
        Assert.notNull(username, "user can't be null");
        Session session = new Session(username);
        try {
            return sessionService.save(session);
        } catch (InternalServerErrorHttpException e) {
            throw new InternalServerErrorHttpException("Failed to login");
        }
    }

    public Session signup(User user) {
        if (userService.isEmailExists(user.getEmail())) {
            throw new ForbiddenHttpException("Can't signup, email already exists:" + user.getEmail());
        }
        if (userService.isUsernameExists(user.getName())) {
            throw new ForbiddenHttpException("Can't signup, username already exists:" + user.getName());
        }
        User dbUser = userService.add(user);
        return login(dbUser.getName());
    }

    public void logout() {
        Session session = getCurrentSession();
        sessionService.delete(session.getId());
    }

    public User getCurrentUser() {
        return userService.get(
                getCurrentSession().getUsername()
        );
    }

    private Session getCurrentSession() {
        SecurityContextImpl securityContext =
                (SecurityContextImpl) SecurityContextHolder.getContext();
        if (securityContext == null || securityContext.getAuthentication() == null) {
            throw new UnauthorizedHttpException("User not authorized");
        }
        return ((Session) securityContext.getAuthentication().getPrincipal());
    }

}
