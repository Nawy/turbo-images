package com.turbo.repository.aerospike.counter;

import com.aerospike.client.*;
import com.aerospike.client.policy.WritePolicy;
import com.turbo.config.AerospikeConfig;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.repository.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by rakhmetov on 21.05.17.
 */
public class AbstractAerospikeCounterRepo {

    private final String binName;
    private final AerospikeClient client;
    private final String databaseName;
    private final String tableName;

    public AbstractAerospikeCounterRepo(
            AerospikeConfig config,
            String tableName
    ) {
        this.client = config.aerospikeClient();
        this.databaseName = config.getDatabaseName();
        this.tableName = tableName;
        this.binName = tableName + "_bin";
    }

    public long incrementAndGet(String key, int expirationInSeconds) {
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.expiration = expirationInSeconds;

        Record record = this.client.operate(
                writePolicy,
                generateKey(key),
                Operation.add(generateBin(1)),
                Operation.get(binName)
        );

        return getRecordValue(record);
    }

    public long get(String key) {
        Record record = client.get(null, generateKey(key));
        return getRecordValue(record);
    }

    private long getRecordValue(Record record) {
        return record != null ?
                (Long) record.getValue(binName) :
                0;
    }

    public void delete(String key) {
        client.delete(null, generateKey(key));
    }

    protected Key generateKey(String key) {
        return new Key(databaseName, tableName, key);
    }

    protected Bin generateBin(int amount) {
        return new Bin(binName, amount);
    }
}
