package com.turbo.config;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rakhmetov on 01/09/16.
 */
@Configuration
public class AerospikeConfig {

    @Value("${aerospike.host}")
    private String host;
    @Value("${aerospike.port}")
    private int port;

    @Bean
    public AerospikeClient aerospikeClient() throws AerospikeException {
        return new AerospikeClient(host, port);
    }
}
