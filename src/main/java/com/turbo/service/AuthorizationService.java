package com.turbo.service;

import com.turbo.model.DeviceType;
import com.turbo.model.Nullable;
import com.turbo.model.Session;
import com.turbo.model.User;
import com.turbo.model.exception.ForbiddenHttpException;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.model.exception.UnauthorizedHttpException;
import com.turbo.repository.aerospike.counter.AuthenticationCounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


@Service
public class AuthorizationService {

    private static final int SECONDS_IN_MINUTES = 60;

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
        this.authAttemptsMinutesTtl = authAttemptsMinutesTtl * SECONDS_IN_MINUTES;
        this.maxAuthAttemptsAmount = maxAuthAttemptsAmount;
    }

    public Session login(String emailOrName, String password, @Nullable DeviceType deviceType, @Nullable String ip) {
        User user;
        isAuthenticationAllowedValidation(emailOrName);
        authenticationCounterRepository.incrementAndGet(emailOrName, authAttemptsMinutesTtl);
        try {
            user = userService.get(emailOrName);
        } catch (NotFoundHttpException e) {
            throw new ForbiddenHttpException("Wrong credentials!");
        }
        if (!user.getPassword().equals(password)) {
            throw new ForbiddenHttpException("Wrong credentials!");
        }
        Session session = login(user.getId(), deviceType, ip);
        authenticationCounterRepository.delete(emailOrName);
        return session;
    }

    private Session login(long userId, @Nullable DeviceType deviceType, @Nullable String ip) {
        DeviceType finalDeviceType = deviceType != null ? deviceType : DeviceType.DEFAULT;
        try {
            return sessionService.save(new Session(userId, finalDeviceType, ip));
        } catch (InternalServerErrorHttpException e) {
            throw new InternalServerErrorHttpException("Failed to login");
        }
    }

    public Session signup(User user, @Nullable DeviceType deviceType, @Nullable String ip) {
        if (userService.isEmailOrNameExists(user.getEmail())) {
            throw new ForbiddenHttpException("Can't signup, email already exists:" + user.getEmail());
        }
        if (userService.isEmailOrNameExists(user.getName())) {
            throw new ForbiddenHttpException("Can't signup, username already exists:" + user.getName());
        }
        User dbUser = userService.add(user);
        return login(dbUser.getId(), deviceType, ip);
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
                getCurrentSession().getUserId()
        );
    }

    public long getCurrentUserId() {
        return getCurrentSession().getUserId();
    }

    private Session getCurrentSession() {
        SecurityContextImpl securityContext = (SecurityContextImpl) SecurityContextHolder.getContext();
        if (securityContext == null || securityContext.getAuthentication() == null) {
            throw new UnauthorizedHttpException("User not authorized");
        }
        return ((Session) securityContext.getAuthentication().getPrincipal());
    }

    private void isAuthenticationAllowedValidation(String usernameOrEmail) {
        long authenticationsAmount = authenticationCounterRepository.get(usernameOrEmail);
        if (authenticationsAmount >= maxAuthAttemptsAmount) {
            throw new ForbiddenHttpException("Too many authorization attempts, wait " + authAttemptsMinutesTtl + " minutes");
        }
    }


}
