package com.turbo.repository.aerospike;

import com.aerospike.client.AerospikeClient;
import com.turbo.model.UserImage;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 06.06.17.
 */
@Repository
public class UserImageRepository extends AbstractAerospikeRepo<UserImage>{

    public UserImageRepository(AerospikeClient client, String namespace) {
        super(client, namespace);
    }
}
