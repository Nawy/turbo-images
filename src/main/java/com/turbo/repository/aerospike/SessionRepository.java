package com.turbo.repository.aerospike;

import com.aerospike.client.policy.WritePolicy;
import com.turbo.config.AerospikeConfig;
import com.turbo.model.Session;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 17.05.17.
 */
@Repository
public class SessionRepository extends AbstractAerospikeRepo<Session> {

    @Autowired
    public SessionRepository(
            AerospikeConfig config,
            @Value("${aerospike.session.table.name}") String tableName
    ) {
        super(config, tableName);
    }

    public Session save(Session entity, int expirationInSeconds) {
        Long id = entity.getId() != null ?
                entity.getId() :
                generateId(entity.getUserId());
        entity.setId(id);

        WritePolicy writePolicy = null;
        if (expirationInSeconds > 0) {
            writePolicy = new WritePolicy();
            writePolicy.expiration = expirationInSeconds;
        }

        client.put(
                writePolicy,
                generateKey(id),
                generateBin(entity)
        );

        return entity;
    }

    private Long generateId(long userId) {
        int iterations = 0;
        do {
            Long id = IdGenerator.generateRandomId(userId);
            if (get(id) == null) return id;
            iterations++;
        } while (ITERATIONS_TO_GENERATE_ID > iterations);
        throw new InternalServerErrorHttpException("Can't save entity");
    }
}
