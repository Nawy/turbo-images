package com.turbo.repository.aerospike.user;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Record;
import com.turbo.config.AerospikeConfig;
import com.turbo.model.User;
import com.turbo.repository.aerospike.AbstractAerospikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 11.06.17.
 */
@Repository
public class UserRepository extends AbstractAerospikeRepo<User> {

    private final String usernameBinName;
    private final String userEmailBinName;
    private final String userIdBinName;
    private final AerospikeClient client;

    @Autowired
    public UserRepository(
            AerospikeConfig config,
            @Value("${aerospike.user.table.name}") String tableName
    ) {
        super(config, tableName);
        this.client = config.aerospikeClient();
        this.usernameBinName = "username_" + tableName;
        this.userEmailBinName = "email_" + tableName;
        this.userIdBinName = "id_" + tableName;
    }

    public User save(User user) {
        Long id = user.getId() != null ?
                user.getId() :
                generateId();
        user.setId(id);


        client.put(
                null,
                generateKey(id),
                generateBin(user),
                generateEmailBin(user),
                generateUsernameBin(user),
                generateIdBin(user)
        );

        return user;
    }

    @SuppressWarnings("unchecked")
    public User get(long id) {
        Record record = client.get(null, generateKey(id));
        return record != null ?
                (User) record.getValue(usernameBinName) :
                null;
    }

    public boolean exists(long id) {
        return client.exists(null, generateKey(id));
    }

    public void delete(long id) {
        client.delete(null, generateKey(id));
    }


    protected Bin generateUsernameBin(User entity) {
        return new Bin(usernameBinName, entity.getName());
    }

    protected Bin generateEmailBin(User entity) {
        return new Bin(userEmailBinName, entity.getEmail());
    }

    protected Bin generateIdBin(User entity) {
        return new Bin(userIdBinName, entity.getId());
    }
}
