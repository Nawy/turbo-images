package com.turbo.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.turbo.model.Session;
import com.turbo.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * Created by rakhmetov on 14.05.17.
 */
@Repository
public class HazelcastSessionRepository {

    private final IMap<Long, User> dbMap;
    private final long ttl;

    @Autowired

    public HazelcastSessionRepository(
            HazelcastInstance hazelcastInstance,
            @Value("${hazelcast.map.name}") String mapName,
            @Value("${hazelcast.map.ttl.seconds}") long ttl
    ) {
        dbMap = hazelcastInstance.getMap(mapName);
        this.ttl = ttl;
    }

    public boolean save(Session session) {
        //check that no record with such random UUID
        if (dbMap.get(session.getId()) != null) {
            return false;
        }
        dbMap.put(session.getId(), session.getUser(), ttl, TimeUnit.SECONDS);
        return true;
    }

    public void remove(long sessionId) {
        dbMap.remove(sessionId);
    }

    public Session get(long sessionId) {
        User user = dbMap.get(sessionId);
        if (user == null) {
            return null;
        }
        return new Session(sessionId, user);
    }
}
