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

    public void add(long id, List<Long> ids) {
        getLargeList(id).add(ids);
    }

    @SuppressWarnings("unchecked")
    public List<Long> get(long id) {
        LargeList llist = getLargeList(id);
        int size = llist.size();
        return (List<Long>) llist.findLast(size);
    }

    public void remove(long id, List<Long> ids) {
        getLargeList(id).remove(ids);
    }

    private LargeList getLargeList(long id) {
        return client.getLargeList(null, generateKey(id), binName);
    }

    protected Key generateKey(long id) {
        return new Key(databaseName, tableName, id);
    }
}
