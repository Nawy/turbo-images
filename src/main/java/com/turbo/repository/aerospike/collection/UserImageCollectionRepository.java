package com.turbo.repository.aerospike.collection;

import com.turbo.config.AerospikeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 26.06.17.
 */
@Repository
//FIXME I'm not needed?
public class UserImageCollectionRepository extends AbstractAerospikeCollectionRepository {

    public UserImageCollectionRepository(
            AerospikeConfig config,
            @Value("${aerospike.user.image.collection.table.name}") String tableName
    ) {
        super(config, tableName);
    }
}
