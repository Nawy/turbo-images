package com.turbo.repository;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;
import com.turbo.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 17.05.17.
 */
@Repository
public class AerospikeCommentRepo {

    private static final String SESSION_BIN = "COMMENT_BIN";

    private final AerospikeClient client;
    private final WritePolicy writePolicy;
    private final String namespace;

    @Autowired
    public AerospikeCommentRepo(
            AerospikeClient client,
            @Value("${aerospike.comment.namespace}") String namespace
    ) {
        this.client = client;
        this.writePolicy = new WritePolicy();
        this.namespace = namespace;
    }

    public void save(long sessionId, Session session) {
        client.put(
                writePolicy,
                generateKey(sessionId),
                generateBin(session)
        );
    }

    public Session get(long sessionId) {
        Record record = client.get(writePolicy, generateKey(sessionId));
        return record != null ?
                (Session) record.getValue(SESSION_BIN) :
                null;
    }

    public boolean contains(long sessionId) {
        return client.exists(writePolicy, generateKey(sessionId));
    }

    public void delete(long sessionId) {
        client.delete(writePolicy, generateKey(sessionId));
    }

    private Key generateKey(long sessionId) {
        return new Key(namespace, null, sessionId);
    }

    private Bin generateBin(Session session) {
        return new Bin(SESSION_BIN, session);
    }

}
