package com.turbo.service;

import com.turbo.model.Session;
import com.turbo.model.exception.ForbiddenHttpException;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.model.exception.UnauthorizedHttpException;
import com.turbo.model.User;
import com.turbo.repository.aerospike.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class AuthorizationService {

    private final SessionRepository sessionRepository;
    private final UserService userService;

    @Autowired
    public AuthorizationService(SessionRepository sessionRepository, UserService userService) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
    }

    public Session getSession(Long sessionId) {
        return sessionRepository.get(sessionId);
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
        return login(user);
    }

    private Session login(User user) {
        Assert.notNull(user, "user can't be null");
        Session session = new Session(user);
        try {
            return sessionRepository.save(session);
        } catch (InternalServerErrorHttpException e) {
            throw new InternalServerErrorHttpException("Failed to login");
        }
    }

    public Session signup(User user) {
        User dbUser = userService.add(user);
        return login(dbUser);
    }

    public void logout() {
        Session session = getCurrentSession();
        sessionRepository.delete(session.getId());
    }

    public User getCurrentUser() {
        return getCurrentSession().getUser();
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
