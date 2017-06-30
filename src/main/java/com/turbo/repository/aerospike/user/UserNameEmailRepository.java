package com.turbo.repository.aerospike.user;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.turbo.config.AerospikeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 11.06.17.
 */
@Repository
public class UserNameEmailRepository {

    private final String userBinName;
    private final AerospikeClient client;
    private final String databaseName;
    private final String tableName;

    @Autowired
    public UserNameEmailRepository(
            AerospikeConfig config,
            @Value("${aerospike.user.email.table.name}") String tableName
    ) {
        this.client = config.aerospikeClient();
        this.databaseName = config.getDatabaseName();
        this.tableName = tableName;
        this.userBinName = tableName + "_binName";
    }

    public void save(String usernameOrEmail,long userId) {
        client.put(
                null,
                generateKey(usernameOrEmail),
                generateEntityBin(userId)
        );
    }

    public Long get(String usernameOrEmail) {
        Record record = client.get(null, generateKey(usernameOrEmail));
        return record != null ?
                (Long) record.getValue(userBinName) :
                null;
    }

    public boolean exists(String usernameOrEmail) {
        return client.exists(null, generateKey(usernameOrEmail));
    }

    public void delete(String usernameOrEmail) {
        client.delete(null, generateKey(usernameOrEmail));
    }

    protected Key generateKey(String usernameOrEmail) {
        return new Key(databaseName, tableName, usernameOrEmail);
    }

    protected Bin generateEntityBin(long userId) {
        return new Bin(userBinName, userId);
    }

}
