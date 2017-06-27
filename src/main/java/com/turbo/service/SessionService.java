package com.turbo.service;

import com.turbo.model.Session;
import com.turbo.repository.aerospike.SessionRepository;
import com.turbo.repository.aerospike.counter.SessionCounterRepository;
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
    private final SessionCounterRepository sessionCounterRepository;
    private final int sessionTtl;
    private final int sessionCounterMinutesTtl;
    private final int sessionCounterAttemptsAmount;

    @Autowired
    public SessionService(
            SessionRepository sessionRepository,
            SessionCounterRepository sessionCounterRepository,
            @Value("${session.days.ttl}") int sessionTtl,
            @Value("${session.creation.attempts.minutes.ttl}") int sessionCounterMinutesTtl,
            @Value("${session.creation.attempts.amount}") int sessionCounterAttemptsAmount
    ) {
        this.sessionRepository = sessionRepository;
        this.sessionCounterRepository = sessionCounterRepository;
        this.sessionTtl = sessionTtl * SECONDS_IN_DAYS;
        this.sessionCounterMinutesTtl = sessionCounterMinutesTtl;
        this.sessionCounterAttemptsAmount = sessionCounterAttemptsAmount;
    }

    public Session save(Session session) {

        return sessionRepository.save(session, sessionTtl);
    }

    public Session get(long sessionId) {
        sessionRepository.touch(sessionId, sessionTtl);
        return sessionRepository.get(sessionId);
    }

    public void delete(long sessionId) {
        sessionRepository.delete(sessionId);
    }

    /*private void sessionCreationVlidation(Session session){
        String username = session.getUsername();
        Integer attemptsAmount = sessionCounterRepository.get(username);
        if (attemptsAmount != null && attemptsAmount > sessionCounterAttemptsAmount)
        sessionCounterRepository.incrementAndGet(session.getUsername(), )
    }*/

    public int getSessionMaxAge(){
        return sessionTtl;
    }
}
