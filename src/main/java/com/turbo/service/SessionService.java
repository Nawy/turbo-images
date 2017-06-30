package com.turbo.service;

import com.turbo.model.Session;
import com.turbo.repository.aerospike.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by rakhmetov on 26.06.17.
 */
@Service
public class SessionService {

    private static final int SECONDS_IN_MINUTES = 60;
    private static final int SECONDS_IN_DAYS = SECONDS_IN_MINUTES * 60 * 24;

    private final SessionRepository sessionRepository;
    private final int sessionTtl;

    @Autowired
    public SessionService(
            SessionRepository sessionRepository,
            @Value("${session.days.ttl}") int sessionTtl
    ) {
        this.sessionRepository = sessionRepository;
        this.sessionTtl = sessionTtl * SECONDS_IN_DAYS;
    }

    public Session save(Session session) {
        sessionRepository.save(session, sessionTtl);
        return session;
    }

    public Session saveIfNotExist(Session session) {
        Session dbSession = get(session.getUserId());
        return dbSession != null ?
                dbSession :
                save(new Session(session.getUserId())); // if session not exists create new
    }

    public Session get(long userId) {
        return sessionRepository.get(userId);
    }

    public void delete(long userId) {
        sessionRepository.delete(userId);
    }

    public int getSessionMaxAge() {
        return sessionTtl;
    }
}
