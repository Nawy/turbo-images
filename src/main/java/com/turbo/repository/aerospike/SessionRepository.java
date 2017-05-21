package com.turbo.repository.aerospike;

import com.aerospike.client.AerospikeClient;
import com.turbo.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 17.05.17.
 */
@Repository
public class SessionRepository extends AbstractAerospikeRepo<Session> {

    private final AerospikeClient client;
    private final String namespace;
    private final int sessionTtl;

    @Autowired
    public SessionRepository(
            AerospikeClient client,
            @Value("${aerospike.session.minutes.ttl}") int sessionTtl,
            @Value("${aerospike.session.namespace}") String namespace
    ) {
        super(client, namespace);
        this.client = client;
        this.namespace = namespace;
        this.sessionTtl = sessionTtl;
    }

    @Override
    public Session save(Session session) {
        return super.save(session, sessionTtl);
    }
}
