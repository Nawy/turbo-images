package com.turbo.service;

import com.turbo.model.Session;
import com.turbo.repository.aerospike.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public Session get(long sessionId) {
        return sessionRepository.get(sessionId);
    }

    public void delete(long sessionId) {
        sessionRepository.delete(sessionId);
    }

}
