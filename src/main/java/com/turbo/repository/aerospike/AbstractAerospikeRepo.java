package com.turbo.repository.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;
import com.turbo.model.IdHolder;
import com.turbo.model.exception.http.InternalServerErrorHttpException;
import com.turbo.repository.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Created by rakhmetov on 21.05.17.
 */
public class AbstractAerospikeRepo<T extends IdHolder & Serializable> {

    protected static final int ITERATIONS_TO_GENERATE_ID = 3;

    private final String defaultBinName;
    private final AerospikeClient client;
    private final String namespace;

    @Autowired
    public AbstractAerospikeRepo(
            AerospikeClient client,
            String namespace
    ) {
        this.client = client;
        this.namespace = namespace;
        this.defaultBinName = namespace + "_binName";
    }

    public T save(T entity) {
        return save(entity, 0);
    }

    public T save(T entity, int expiration) {
        Long sessionId = entity.getId() != null ?
                entity.getId() :
                generateId();
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

    @SuppressWarnings("unchecked")
    public T get(long id) {
        Record record = client.get(null, generateKey(id));
        return record != null ?
                (T) record.getValue(defaultBinName) :
                null;
    }

    public boolean exists(long id) {
        return client.exists(null, generateKey(id));
    }

    public void delete(long id) {
        client.delete(null, generateKey(id));
    }

    protected Key generateKey(long id) {
        return new Key(namespace, null, id);
    }

    protected Bin generateBin(T entity) {
        return new Bin(defaultBinName, entity);
    }

    protected Long generateId() {
        int iterations = 0;
        do {
            Long id = IdGenerator.generateRandomId();
            if (get(id) == null) return id;
            iterations++;
        } while (ITERATIONS_TO_GENERATE_ID > iterations);
        throw new InternalServerErrorHttpException("Can't save entity");
    }
}
