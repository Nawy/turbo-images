package com.turbo.service;

import com.turbo.model.Session;
import com.turbo.model.exception.ForbiddenHttpException;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.model.exception.UnauthorizedHttpException;
import com.turbo.model.user.User;
import com.turbo.repository.aerospike.SessionRepository;
import com.turbo.repository.couchbase.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Service
public class AuthorisationService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthorisationService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public Session getSession(long sessionId) {
        return sessionRepository.get(sessionId);
    }

    public Session login(String email, String password) {
        User user;
        try {
            user = userRepository.findByEmail(email);
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
        User dbUser = userRepository.save(user);
        return login(dbUser);
    }

    public User getCurrentUser() {
        SecurityContextImpl securityContext =
                (SecurityContextImpl) SecurityContextHolder.getContext();
        if (securityContext == null || securityContext.getAuthentication() == null) {
            throw new UnauthorizedHttpException("User not authorized");
        }
        return ((Session) securityContext.getAuthentication().getPrincipal()).getUser();
    }
}
