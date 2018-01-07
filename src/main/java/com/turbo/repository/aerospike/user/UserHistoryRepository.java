package com.turbo.repository.aerospike.user;

import com.aerospike.client.AerospikeClient;
import com.turbo.config.AerospikeConfig;
import com.turbo.model.UserHistory;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.repository.aerospike.AbstractAerospikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by rakhmetov on 11.06.17.
 */
@Repository
public class UserHistoryRepository extends AbstractAerospikeRepo<UserHistory> {

    private final AerospikeClient client;

    @Autowired
    public UserHistoryRepository(
            AerospikeConfig config,
            @Value("${aerospike.user.stat.table.name}") String tableName
    ) {
        super(config, tableName);
        this.client = config.aerospikeClient();
    }

    public UserHistory save(UserHistory userHistory) {
        if (userHistory.getId() == null) throw new InternalServerErrorHttpException("user stats can't have null user id");

        client.put(
                null,
                generateKey(userHistory.getId()),
                generateBin(userHistory)
        );

        return userHistory;
    }
}
