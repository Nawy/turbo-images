package com.turbo.repository.aerospike;

import com.aerospike.client.AerospikeClient;
import com.turbo.model.comment.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 17.05.17.
 */
@Repository
public class CommentRepository extends AbstractAerospikeRepo<Comment> {

    @Autowired
    public CommentRepository(
            AerospikeClient client,
            @Value("${aerospike.comment.namespace}") String namespace
    ) {
        super(client, namespace);
    }
}
