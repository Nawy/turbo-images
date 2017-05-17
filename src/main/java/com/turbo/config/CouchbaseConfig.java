package com.turbo.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Objects;

/**
 * Created by ermolaev on 5/17/17.
 */
@Configuration
@ConfigurationProperties(prefix = "couchbase")
public class CouchbaseConfig {

    private String[] clusters;
    private String bucketName;

    private Cluster cluster;
    private Bucket bucket;

    @PostConstruct
    public void init() {
        Objects.requireNonNull(clusters, "Couchbase clusters in properties cannot be empty");
        this.cluster = CouchbaseCluster.create(clusters);
        this.bucket = cluster.openBucket(bucketName);
    }

    @PreDestroy
    public void closeConnections() {
        this.bucket.close();
        this.cluster.disconnect();
    }

    public String[] getClusters() {
        return clusters;
    }

    public void setClusters(String[] clusters) {
        this.clusters = clusters;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
