package com.turbo.config;

import org.apache.http.util.Asserts;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Created by ermolaev on 5/15/17.
 */
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticsearchConfig {
    private static final int MAX_SIZE_OF_SELECT_LIST = 300;

    private String[] hosts;
    private String clusterName;

    private String searchPostIndexName;
    private String searchPostTypeName;

    private String searchUserIndexName;
    private String searchUserTypeName;

    private String statPostsIndexName;
    private String statPostsTypeName;

    private int maxSizePostsPerPage;
    private int maxSizeUsersPerPage;

    private TransportClient elasticClient;

    public ElasticsearchConfig() {
    }

    @PostConstruct
    public void init() {
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        try {
            InetSocketTransportAddress[] addresses = getAddresses();
            elasticClient = new PreBuiltTransportClient(settings).addTransportAddresses(addresses);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Can't create elastic transport client!", e);
        }

        Asserts.check(maxSizePostsPerPage == 0, "Max size post per page cannot be 0");
        Asserts.check(maxSizeUsersPerPage == 0, "Max size users per page cannot be 0");
        Asserts.check(maxSizePostsPerPage >= MAX_SIZE_OF_SELECT_LIST, "Max size posts per page cannot be more than " + MAX_SIZE_OF_SELECT_LIST);
        Asserts.check(maxSizeUsersPerPage >= MAX_SIZE_OF_SELECT_LIST, "Max size users per page cannot be more than " + MAX_SIZE_OF_SELECT_LIST);
    }

    public TransportClient getElasticClient() {
        return elasticClient;
    }

    private InetSocketTransportAddress[] getAddresses() throws UnknownHostException {
        Objects.requireNonNull(hosts, "Elasticsearch hosts in properties cannot be empty");
        InetSocketTransportAddress[] addresses = new InetSocketTransportAddress[hosts.length];
        for(int i = 0; i < hosts.length; i++) {
            String[] addr = hosts[i].split(":");
            addresses[i] = new InetSocketTransportAddress(
                    InetAddress.getByName(addr[0]),
                    Integer.valueOf(addr[1])
            );
        }

        return addresses;
    }

    public String[] getHosts() {
        return hosts;
    }

    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getSearchPostIndexName() {
        return searchPostIndexName;
    }

    public void setSearchPostIndexName(String searchPostIndexName) {
        this.searchPostIndexName = searchPostIndexName;
    }

    public String getSearchPostTypeName() {
        return searchPostTypeName;
    }

    public void setSearchPostTypeName(String searchPostTypeName) {
        this.searchPostTypeName = searchPostTypeName;
    }

    public String getSearchUserIndexName() {
        return searchUserIndexName;
    }

    public void setSearchUserIndexName(String searchUserIndexName) {
        this.searchUserIndexName = searchUserIndexName;
    }

    public String getSearchUserTypeName() {
        return searchUserTypeName;
    }

    public void setSearchUserTypeName(String searchUserTypeName) {
        this.searchUserTypeName = searchUserTypeName;
    }

    public int getMaxSizePostsPerPage() {
        return maxSizePostsPerPage;
    }

    public void setMaxSizePostsPerPage(int maxSizePostsPerPage) {
        this.maxSizePostsPerPage = maxSizePostsPerPage;
    }

    public int getMaxSizeUsersPerPage() {
        return maxSizeUsersPerPage;
    }

    public void setMaxSizeUsersPerPage(int maxSizeUsersPerPage) {
        this.maxSizeUsersPerPage = maxSizeUsersPerPage;
    }

    public void setElasticClient(TransportClient elasticClient) {
        this.elasticClient = elasticClient;
    }

    public String getStatPostsIndexName() {
        return statPostsIndexName;
    }

    public void setStatPostsIndexName(String statPostsIndexName) {
        this.statPostsIndexName = statPostsIndexName;
    }

    public String getStatPostsTypeName() {
        return statPostsTypeName;
    }

    public void setStatPostsTypeName(String statPostsTypeName) {
        this.statPostsTypeName = statPostsTypeName;
    }
}
