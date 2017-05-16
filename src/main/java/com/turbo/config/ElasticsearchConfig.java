package com.turbo.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by ermolaev on 5/15/17.
 */
@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.hosts}")
    private List<String> hosts;

    @Value("${elasticsearch.cluster-name}")
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
        InetSocketTransportAddress[] addresses = new InetSocketTransportAddress[hosts.size()];
        for(int i = 0; i < hosts.size(); i++) {
            String[] datas = hosts.get(i).split(":");
            addresses[i] = new InetSocketTransportAddress(
                    InetAddress.getByName(datas[0]),
                    Integer.valueOf(datas[1])
            );
        }

        return addresses;
    }
}
