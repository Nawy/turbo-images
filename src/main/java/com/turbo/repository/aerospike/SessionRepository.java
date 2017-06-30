package com.turbo.repository.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;
import com.turbo.config.AerospikeConfig;
import com.turbo.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 17.05.17.
 */
@Repository
public class SessionRepository {

    private final String binName;
    private final AerospikeClient client;
    private final String databaseName;
    private final String tableName;

    @Autowired
    public SessionRepository(
            AerospikeConfig config,
            @Value("${aerospike.session.table.name}") String tableName
    ) {
        this.client = config.aerospikeClient();
        this.databaseName = config.getDatabaseName();
        this.tableName = tableName;
        this.binName = tableName + "_bin";
    }

    public void save(Session session, int expirationInSeconds) {
        WritePolicy writePolicy = null;
        if (expirationInSeconds > 0) {
            writePolicy = new WritePolicy();
            writePolicy.expiration = expirationInSeconds;
        }

        client.put(
                writePolicy,
                generateKey(session.getUserId()),
                generateBin(session)
        );
    }

    public Session get(long userId) {
        Record record = client.get(null, generateKey(userId));
        return record != null ?
                (Session) record.getValue(binName) :
                null;
    }

    public void delete(long userId) {
        client.delete(null, generateKey(userId));
    }

    protected Key generateKey(long userId) {
        return new Key(databaseName, tableName, userId);
    }

    protected Bin generateBin(Session session) {
        return new Bin(binName, session);
    }

}
