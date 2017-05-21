package com.turbo.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * Created by ermolaev on 5/17/17.
 */
@Configuration
@ConfigurationProperties(prefix = "couchbase")
public class CouchbaseConfig {

    private String[] clusters;
    private String postBucketName;
    private String userBucketName;

    @Bean(destroyMethod = "disconnect")
    public Cluster cluster() {
        Objects.requireNonNull(clusters, "Couchbase clusters in properties cannot be empty");
        return CouchbaseCluster.create(clusters);
    }

    @Bean(name = "post-bucket", destroyMethod = "close")
    public Bucket postBucket(Cluster cluster) {
        return cluster.openBucket(postBucketName);
    }

    @Bean(name = "user-bucket", destroyMethod = "close")
    public Bucket userBucket(Cluster cluster) {
        return cluster.openBucket(userBucketName);
    }
}
