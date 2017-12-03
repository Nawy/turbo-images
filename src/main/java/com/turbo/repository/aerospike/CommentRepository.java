package com.turbo.repository.aerospike;

import com.turbo.config.AerospikeConfig;
import com.turbo.model.aerospike.CommentRepoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 17.05.17.
 */
@Repository
public class CommentRepository extends AbstractAerospikeRepo<CommentRepoModel> {

    @Autowired
    public CommentRepository(
            AerospikeConfig config,
            @Value("${aerospike.comment.table.name}") String tableName
    ) {
        super(config, tableName);
    }
}
