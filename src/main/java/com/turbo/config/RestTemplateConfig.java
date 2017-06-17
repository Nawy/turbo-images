package com.turbo.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Created by ermolaev on 2/8/17.
 */
@Configuration
public class RestTemplateConfig {

    private static final int MILLISECONDS_PER_SECOND = 1000;

    @Value("${server.jetty.acceptors}")
    private int jettyAcceptors;
    @Value("${http.connection.timeout}")
    private int connectionTimeOut;

    @Bean
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public RestTemplate restTemplate(
            @Qualifier("wrapperRestTemplateInterceptor") ClientHttpRequestInterceptor clientHttpRequestInterceptor
    ) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(connectionTimeOut * MILLISECONDS_PER_SECOND)
                .build();
        org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(jettyAcceptors)
                .setDefaultRequestConfig(requestConfig)
                .useSystemProperties()
                .build();
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
        restTemplate.setInterceptors(Collections.singletonList(clientHttpRequestInterceptor));
        return restTemplate;
    }

}
