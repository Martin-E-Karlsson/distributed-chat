package com.chat.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class DownstreamConfig {

    @Bean
    public RestClient authRestClient(@Value("${downstream.auth-service.url}") String url) {
        return RestClient.builder().baseUrl(url).build();
    }

    @Bean
    public RestClient userRestClient(@Value("${downstream.user-service.url}") String url) {
        return RestClient.builder().baseUrl(url).build();
    }

    @Bean
    public RestClient messageRestClient(@Value("${downstream.message-service.url}") String url) {
        return RestClient.builder().baseUrl(url).build();
    }
}
