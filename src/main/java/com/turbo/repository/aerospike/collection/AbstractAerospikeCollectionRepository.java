package com.turbo.repository.aerospike.collection;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.large.LargeList;
import com.turbo.config.AerospikeConfig;

import java.util.List;

/**
 * Created by rakhmetov on 26.06.17.
 */
public class AbstractAerospikeCollectionRepository {

    private final String binName;
    private final AerospikeClient client;
    private final String databaseName;
    private final String tableName;


    public AbstractAerospikeCollectionRepository(
            AerospikeConfig config,
            String tableName
    ) {
        this.client = config.aerospikeClient();
        this.databaseName = config.getDatabaseName();
        this.tableName = tableName;
        this.binName = tableName + "_bin";
    }

    public void add(String username, List<Long> ids) {
        getLargeList(username).add(ids);
    }

    @SuppressWarnings("unchecked")
    public List<Long> get(String username) {
        LargeList llist = getLargeList(username);
        int size = llist.size();
        return (List<Long>) llist.findLast(size);
    }

    public void remove(String username, List<Long> ids) {
        getLargeList(username).remove(ids);
    }

    private LargeList getLargeList(String username) {
        return client.getLargeList(null, generateKey(username), binName);
    }

    protected Key generateKey(String username) {
        return new Key(databaseName, tableName, username);
    }
}
