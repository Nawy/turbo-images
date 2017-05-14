package com.turbo.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rakhmetov on 14.05.17.
 */
@Configuration
public class HazelcastConfig {

    @Bean
    public HazelcastInstance hazelcastInstance(
            @Value("${hazelcast.address}") String hazelcastAddress,
            @Value("${hazelcast.pool.size}") int poolSize
    ) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setExecutorPoolSize(
                poolSize
        );
        clientConfig.setNetworkConfig(
                new ClientNetworkConfig() {{
                    addAddress(hazelcastAddress);
                }}
        );

        return HazelcastClient.newHazelcastClient(clientConfig);
    }

}
