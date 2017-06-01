package com.turbo.repository.aerospike;

import com.aerospike.client.AerospikeClient;
import com.turbo.model.Image;
import com.turbo.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 21.05.17.
 */
@Repository
public class PostRepository extends AbstractAerospikeRepo<Post>  {

    @Autowired
    public PostRepository(
            AerospikeClient client,
            @Value("${aerospike.post.namespace}") String namespace
    ) {
        super(client, namespace);
    }
}
