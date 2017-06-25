package com.turbo.repository.aerospike;

import com.turbo.config.AerospikeConfig;
import com.turbo.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 17.05.17.
 */
@Repository
public class SessionRepository extends AbstractAerospikeRepo<Session> {

    private final int sessionTtl;
    private static final int SECONDS_IN_MINUTES = 60;

    @Autowired
    public SessionRepository(
            AerospikeConfig config,
            @Value("${aerospike.session.minutes.ttl}") int sessionTtl,
            @Value("${aerospike.session.table.name}") String tableName
    ) {
        super(config, tableName);
        this.sessionTtl = sessionTtl;
    }

    @Override
    public Session save(Session session) {
        return super.save(session, sessionTtl * SECONDS_IN_MINUTES);
    }
}
