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

    private final static String USERNAME_BIN_NAME = "username";
    private final static String USER_EMAIL_BIN_NAME = "email";
    private final static String USER_ID_BIN_NAME = "id";
    private final static String USER_ENTITY_BIN_NAME = "entity";

    private final AerospikeClient client;

    @Autowired
    public UserRepository(
            AerospikeConfig config,
            @Value("${aerospike.user.table.name}") String tableName
    ) {
        super(config, tableName);
        this.client = config.aerospikeClient();
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
                generateUserBin(user),
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
                (User) record.getValue(USER_ENTITY_BIN_NAME) :
                null;
    }

    public boolean exists(long id) {
        return client.exists(null, generateKey(id));
    }

    public void delete(long id) {
        client.delete(null, generateKey(id));
    }


    protected Bin generateUserBin(User user) {
        return new Bin(USER_ENTITY_BIN_NAME, user);
    }

    protected Bin generateUsernameBin(User entity) {
        return new Bin(USERNAME_BIN_NAME, entity.getName());
    }

    protected Bin generateEmailBin(User entity) {
        return new Bin(USER_EMAIL_BIN_NAME, entity.getEmail());
    }

    protected Bin generateIdBin(User entity) {
        return new Bin(USER_ID_BIN_NAME, entity.getId());
    }
}
