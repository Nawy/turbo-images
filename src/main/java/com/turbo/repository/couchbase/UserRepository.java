package com.turbo.repository.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.model.user.User;
import com.turbo.repository.util.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ermolaev on 5/17/17.
 */
@Repository
public class UserRepository extends AbstractCouchbaseRepository<User> {

    private static final String EMAIL_FIELD_NAME = "email";

    private final Bucket bucket;

    @Autowired
    public UserRepository(@Qualifier("user-bucket") Bucket bucket, JsonParser jsonParser) {
        super(bucket, jsonParser, User.class);
        this.bucket = bucket;
        bucket.bucketManager().createN1qlIndex("email_index", true, false, EMAIL_FIELD_NAME);
    }

    public User findByEmail(String email) {

        List<N1qlQueryRow> result = bucket.query(
                N1qlQuery.parameterized(
                        "SELECT * FROM $base_name WHERE email=$email LIMIT 1",
                        JsonArray.from(bucket.name(), email)
                )
        ).allRows();

        if (result.isEmpty()) {
            throw new NotFoundHttpException("User not found for email:" + email);
        }
        return parse(
                result.get(0).value()
        );
    }
}
