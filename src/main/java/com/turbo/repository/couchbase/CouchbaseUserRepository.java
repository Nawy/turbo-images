package com.turbo.repository.couchbase;

import com.couchbase.client.java.Bucket;
import com.turbo.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Created by ermolaev on 5/17/17.
 */
@Repository
public class CouchbaseUserRepository extends AbstractCouchbaseRepository<User> {

    private final Bucket bucket;

    @Autowired
    public CouchbaseUserRepository(@Qualifier("user-bucket") Bucket bucket) {
        super(bucket, User.class);
        this.bucket = bucket;
    }
}
