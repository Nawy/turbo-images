package com.turbo.repository.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.turbo.model.IdHolder;
import com.turbo.repository.util.JsonParser;

/**
 * Created by rakhmetov on 21.05.17.
 */
public abstract class AbstractCouchbaseRepository<T extends IdHolder> {

    protected final JsonParser jsonParser;
    protected final Bucket bucket;
    private final String collectionName;
    private final Class<T> clazz;

    public AbstractCouchbaseRepository(Bucket bucket, JsonParser jsonParser, Class<T> clazz) {
        this.jsonParser = jsonParser;
        this.bucket = bucket;
        this.collectionName = bucket.name();
        this.clazz = clazz;
        bucket.bucketManager().createN1qlPrimaryIndex(true, false);
    }

    public T save(T entity) {
        return save(entity, 0);
    }

    /**
     * Write entity to document collection (List)
     *
     * @param expiry
     * @param entity
     */
    public T save(T entity, int expiry) {
        generateIdIfNotExists(entity);
        bucket.upsert(
                write(entity, expiry)
        );
        return entity;
    }

    public boolean exists(long id) {
        return bucket.exists(Long.toString(id));
    }

    public T get(String id) {
        JsonDocument jsonDocument = bucket.get(id);
        return parse(jsonDocument);
    }


    protected T parse(JsonDocument jsonDocument) {
        return jsonParser.parse(
                jsonDocument.content().toString(),
                clazz
        );
    }

    protected T parse(JsonObject jsonObject) {
        return jsonParser.parse(
                jsonObject.toString(),
                clazz
        );
    }

    protected JsonDocument write(IdHolder entity, int expiry) {
        JsonObject jsonObject = JsonObject.fromJson(jsonParser.write(entity));
        return JsonDocument.create(
                entity.getId().toString(),
                expiry,
                jsonObject
        );
    }

    private void generateIdIfNotExists(T entity) {
        if (entity.getId() == null) {
            entity.setId(
                    generateId(collectionName)
            );
        }
    }

    protected String generateId(String collectionName) {
        return bucket.counter(collectionName + "-counter", 1).content().toString();
    }
}
