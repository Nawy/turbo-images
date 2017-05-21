package com.turbo.repository.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;
import com.turbo.model.Session;
import com.turbo.model.comment.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 17.05.17.
 */
@Repository
public class CommentRepository extends AbstractAerospikeRepo<Comment> {

    private final AerospikeClient client;
    private final String namespace;

    @Autowired
    public CommentRepository(
            AerospikeClient client,
            @Value("${aerospike.comment.namespace}") String namespace
    ) {
        super(client, namespace);
        this.client = client;
        this.namespace = namespace;
    }

}
