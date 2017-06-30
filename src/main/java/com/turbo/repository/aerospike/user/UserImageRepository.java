package com.turbo.repository.aerospike.user;

import com.turbo.config.AerospikeConfig;
import com.turbo.model.UserImage;
import com.turbo.model.aerospike.UserImageContent;
import com.turbo.repository.aerospike.AbstractAerospikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 06.06.17.
 */
@Repository
public class UserImageRepository extends AbstractAerospikeRepo<UserImageContent> {

    @Autowired
    public UserImageRepository(
            AerospikeConfig config,
            @Value("${aerospike.user.image.table.name}") String tableName) {
        super(config, tableName);
    }
}
