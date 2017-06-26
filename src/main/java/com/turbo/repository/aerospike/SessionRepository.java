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

    @Autowired
    public SessionRepository(
            AerospikeConfig config,
            @Value("${aerospike.session.table.name}") String tableName
    ) {
        super(config, tableName);
    }
}
