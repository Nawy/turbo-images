package com.turbo.repository.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.large.LargeList;
import com.turbo.config.AerospikeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rakhmetov on 26.06.17.
 */
@Repository
public class UserImageCollectionRepository {

    private final String binName;
    private final AerospikeClient client;
    private final String databaseName;
    private final String tableName;


    public UserImageCollectionRepository(
            AerospikeConfig config,
            @Value("${aerospike.user.image.collection.table.name}") String tableName
    ) {
        this.client = config.aerospikeClient();
        this.databaseName = config.getDatabaseName();
        this.tableName = tableName;
        this.binName = tableName + "_bin";
    }

    public void add(String username, List<Long> userImageIds) {
        LargeList llist = client.getLargeList(null, generateKey(username), binName);
        llist.add(userImageIds);
    }

    public List<Long> get(String username) {
        LargeList llist = client.getLargeList(null, generateKey(username), binName);
        int size = llist.size();
        return (List<Long>) llist.findLast(size);
    }

    public void remove(String username, List<Long> userImageId) {
        LargeList llist = client.getLargeList(null, generateKey(username), binName);
        llist.remove(userImageId);
    }

    protected Key generateKey(String id) {
        return new Key(databaseName, tableName, id);
    }
}
