package com.turbo.repository.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.policy.WritePolicy;
import com.turbo.model.comment.Comment;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.repository.util.UserIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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

    // protection from generating existing id's
    protected String generateId(String userId) {
        int iterations = 0;
        do {
            String id = UserIdGenerator.generateCommentKey(userId);
            if (get(id) == null) return id;
            iterations++;
        } while (ITERATIONS_TO_GENERATE_ID > iterations);
        throw new InternalServerErrorHttpException("Can't save entity");
    }

    @Override
    public Comment save(Comment entity) {
        return save(entity, 0);
    }

    @Override
    public Comment save(Comment entity, int expiration) {
        String sessionId = entity.getId() != null ?
                entity.getId() :
                generateId(entity.getAuthorId());
        entity.setId(sessionId);

        WritePolicy writePolicy = null;
        if (expiration > 0) {
            writePolicy = new WritePolicy();
            writePolicy.expiration = expiration;
        }

        client.put(
                writePolicy,
                generateKey(sessionId),
                generateBin(entity)
        );

        return entity;
    }

}
