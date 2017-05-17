package com.turbo.config;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rakhmetov on 01/09/16.
 */
@Configuration
@ConfigurationProperties(prefix = "aerospike")
public class AerospikeConfig {

    private String host;
    private int port;

    @Bean
    public AerospikeClient aerospikeClient() throws AerospikeException {
        return new AerospikeClient(host, port);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
