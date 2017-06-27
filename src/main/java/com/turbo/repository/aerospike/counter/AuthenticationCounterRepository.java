package com.turbo.repository.aerospike.counter;

import com.turbo.config.AerospikeConfig;
import com.turbo.model.Session;
import com.turbo.repository.aerospike.AbstractAerospikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 17.05.17.
 */
@Repository
public class AuthenticationCounterRepository extends AbstractAerospikeCounterRepo {

    @Autowired
    public AuthenticationCounterRepository(
            AerospikeConfig config,
            @Value("${aerospike.authorization.counter.table.name}") String tableName
    ) {
        super(config, tableName);
    }
}
