package com.turbo.repository.aerospike;

import com.turbo.config.AerospikeConfig;
import com.turbo.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 21.05.17.
 */
@Repository
public class PostRepository extends AbstractAerospikeRepo<Post> {

    @Autowired
    public PostRepository(
            AerospikeConfig config,
            @Value("${aerospike.post.table.name}") String tableName
    ) {
        super(config, tableName);
    }
}
