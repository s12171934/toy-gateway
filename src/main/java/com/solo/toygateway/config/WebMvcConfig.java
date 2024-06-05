package com.solo.toygateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebMvcConfig {

    @LoadBalanced
    @Bean
    public WebClient.Builder webClientBuilder() { //eureka의 로드밸런싱을 사용하기 위해 webclient 설정

        return WebClient.builder();
    }
}
