package com.turbo.service;

import com.turbo.model.Session;
import com.turbo.repository.aerospike.SessionRepository;
import com.turbo.repository.aerospike.collection.SessionCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Created by rakhmetov on 26.06.17.
 */
@Service
public class SessionService {

    private static final int SECONDS_IN_DAYS = 60 * 60 * 24;

    private final SessionRepository sessionRepository;
    private final SessionCollectionRepository sessionCollectionRepository;// TODO REMOVE?
    private final int sessionTtlDays;

    @Autowired
    public SessionService(
            SessionRepository sessionRepository,
            SessionCollectionRepository sessionCollectionRepository,
            @Value("${session.days.ttl}") int sessionTtlDays
    ) {
        this.sessionRepository = sessionRepository;
        this.sessionCollectionRepository = sessionCollectionRepository;
        this.sessionTtlDays = sessionTtlDays;
    }

    public Session save(Session session) {
        return sessionRepository.save(session, sessionTtlDays * SECONDS_IN_DAYS);
    }

    public Session get(long sessionId) {
        sessionRepository.touch(sessionId, sessionTtlDays * SECONDS_IN_DAYS);
        return sessionRepository.get(sessionId);
    }

    public void delete(long sessionId){
        sessionRepository.delete(sessionId);
    }
}
