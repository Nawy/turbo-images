package com.turbo.service;

import com.turbo.model.Session;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.exception.UnauthorizedHttpException;
import com.turbo.model.user.User;
import com.turbo.repository.HazelcastSessionRepository;
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

    private final HazelcastSessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthorisationService(HazelcastSessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public Session getSession(long sessionId) {
        return sessionRepository.findOne(sessionId);
    }

    public Session login(String email, String password) {
        User user = userRepository.findUser(email);
        return login(user);
    }

    private Session login(User user) {
        Assert.notNull(user, "user can't be null");
        Session session = new Session(user);
        int maxRetryCount = 3;
        for (int i = 0; i < maxRetryCount; i++) {
            boolean saveSuccess = sessionRepository.save(session);
            if (saveSuccess) {
                return session;
            } else {
                // every constructor creation generates new random UUID
                session = new Session(user);
            }
        }
        throw new InternalServerErrorHttpException("Failed to login");
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
