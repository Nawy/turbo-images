package com.turbo.repository.aerospike;

import com.aerospike.client.AerospikeClient;
import com.turbo.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 21.05.17.
 */
@Repository
public class ImageRepository extends AbstractAerospikeRepo<Image>  {

    private final AerospikeClient client;
    private final String namespace;

    @Autowired
    public ImageRepository(
            AerospikeClient client,
            @Value("${aerospike.image.namespace}") String namespace
    ) {
        super(client, namespace);
        this.client = client;
        this.namespace = namespace;
    }
}
