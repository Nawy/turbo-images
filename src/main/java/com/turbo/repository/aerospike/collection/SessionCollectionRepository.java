package com.turbo.repository.aerospike.collection;

import com.turbo.config.AerospikeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 26.06.17.
 * Key is username value session ids
 */
@Repository
public class SessionCollectionRepository extends AbstractAerospikeCollectionRepository {

    public SessionCollectionRepository(
            AerospikeConfig config,
            @Value("${aerospike.session.collection.table.name}") String tableName
    ) {
        super(config, tableName);
    }
}
