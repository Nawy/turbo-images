package com.turbo.service;

import com.turbo.model.Session;
import com.turbo.model.User;
import com.turbo.model.exception.ForbiddenHttpException;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.model.exception.UnauthorizedHttpException;
import com.turbo.repository.aerospike.counter.AuthenticationCounterRepository;
import netscape.security.ForbiddenTargetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


@Service
public class AuthorizationService {

    private static final int SECOND_IN_MINUTES = 60;

    private final SessionService sessionService;
    private final UserService userService;
    private final AuthenticationCounterRepository authenticationCounterRepository;
    private final int authAttemptsMinutesTtl;
    private final int maxAuthAttemptsAmount;

    @Autowired
    public AuthorizationService(
            SessionService sessionService,
            UserService userService,
            AuthenticationCounterRepository authenticationCounterRepository,
            @Value("${auth.attempts.minutes.ttl}") int authAttemptsMinutesTtl,
            @Value("${auth.attempts.amount}") int maxAuthAttemptsAmount
    ) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.authenticationCounterRepository = authenticationCounterRepository;
        this.authAttemptsMinutesTtl = authAttemptsMinutesTtl * SECOND_IN_MINUTES;
        this.maxAuthAttemptsAmount = maxAuthAttemptsAmount;
    }

    public Session login(String email, String password) {
        User user;
        isAuthenticationAllowedValidation(email);
        authenticationCounterRepository.incrementAndGet(email, authAttemptsMinutesTtl);
        try {
            user = userService.findByEmail(email);
        } catch (NotFoundHttpException e) {
            throw new ForbiddenHttpException("Wrong credentials!");
        }
        if (!user.getPassword().equals(password)) {
            throw new ForbiddenHttpException("Wrong credentials!");
        }
        Session session = login(user.getName());
        authenticationCounterRepository.delete(email);
        return session;
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

    /**
     * @return deleted session id
     */
    public long logout() {
        Session session = getCurrentSession();
        sessionService.delete(session.getId());
        return session.getId();
    }

    public User getCurrentUser() {
        return userService.get(
                getCurrentSession().getUsername()
        );
    }

    private Session getCurrentSession() {
        SecurityContextImpl securityContext = (SecurityContextImpl) SecurityContextHolder.getContext();
        if (securityContext == null || securityContext.getAuthentication() == null) {
            throw new UnauthorizedHttpException("User not authorized");
        }
        return ((Session) securityContext.getAuthentication().getPrincipal());
    }

    private void isAuthenticationAllowedValidation(String username) {
        long authenticationsAmount = authenticationCounterRepository.get(username);
        if (authenticationsAmount >= maxAuthAttemptsAmount) {
            throw new ForbiddenHttpException("Too many authorization attempts, wait " + authAttemptsMinutesTtl + " minutes");
        }
    }
}
