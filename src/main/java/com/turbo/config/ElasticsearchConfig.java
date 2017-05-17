package com.turbo.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

/**
 * Created by ermolaev on 5/15/17.
 */
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticsearchConfig {

    private String[] hosts;
    private String clusterName;

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
    }

    @PreDestroy
    public void closeConnection() {
        elasticClient.close();
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
}
