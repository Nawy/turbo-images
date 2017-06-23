package com.turbo.repository.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexType;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
import com.aerospike.client.task.IndexTask;
import com.turbo.config.AerospikeConfig;
import com.turbo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by rakhmetov on 11.06.17.
 */
@Repository
public class UserRepository {

    private final String userNamespace;
    private final String userEmailNamespace;
    private final AerospikeClient client;
    private final String databaseName;
    private final String tableName;

    @Autowired
    public UserRepository(
            AerospikeConfig config,
            @Value("${aerospike.user.table.name}") String tableName
    ) {
        this.client = config.aerospikeClient();
        this.databaseName = config.getDatabaseName();
        this.tableName = tableName;
        this.userNamespace = tableName + "_binName";
        this.userEmailNamespace = "email_" + tableName;
    }

    @PostConstruct
    private void init() {
        // create index for search by email queries
        IndexTask task = client.createIndex(
                null,
                databaseName,
                tableName,
                userEmailNamespace + "_index",
                userEmailNamespace,
                IndexType.STRING
        );

        task.waitTillComplete(100);
    }

    public User save(User user) {
        String entityID = user.getName();

        client.put(
                null,
                generateKey(entityID),
                generateEntityBin(user),
                generateEmailBin(user)
        );

        return user;
    }

    public User getByEmail(String email) {
        Statement stmt = new Statement();
        stmt.setNamespace(databaseName);
        stmt.setSetName(tableName);
        stmt.setBinNames(userEmailNamespace);
        stmt.setFilter(Filter.equal(userEmailNamespace, email));

        try (RecordSet rs = client.query(null, stmt)) {

            if (rs.next()) {
                return (User) rs.getRecord().getValue(userNamespace);
            } else {
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<User> bulkGet(String[] names) {
        Key[] keys = (Key[]) Arrays.stream(names).map(this::generateKey).toArray();
        Record[] records = client.get(null, keys);
        return Arrays.stream(records)
                .filter(Objects::nonNull)
                .map(record -> (User) record.getValue(userNamespace))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public User get(String name) {
        Record record = client.get(null, generateKey(name));
        return record != null ?
                (User) record.getValue(userNamespace) :
                null;
    }

    public boolean exists(String name) {
        return client.exists(null, generateKey(name));
    }

    public void delete(String name) {
        client.delete(null, generateKey(name));
    }

    protected Key generateKey(String id) {
        return new Key(databaseName, tableName, id);
    }

    protected Bin generateEntityBin(User entity) {
        return new Bin(userNamespace, entity);
    }

    protected Bin generateEmailBin(User entity) {
        return new Bin(userEmailNamespace, entity.getEmail());
    }

}
