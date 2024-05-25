package com.solo.toygateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebMvcConfig {
    @LoadBalanced
    @Bean
    public WebClient.Builder webClientBuilder() {

        return WebClient.builder();
    }
}
