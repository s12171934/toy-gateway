package com.solo.toygateway.filter;

import com.solo.toygateway.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JWTFilter implements GlobalFilter {

    private final JWTUtil jwtUtil;
    private final WebClient.Builder webClientBuilder;

    public JWTFilter(JWTUtil jwtUtil, WebClient.Builder webClientBuilder ) {

        this.jwtUtil = jwtUtil;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = exchange.getRequest().getHeaders().getFirst("access");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {

            return chain.filter(exchange);
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        //access token인지 확인
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        //passport 발급
        Mono<String> test = webClientBuilder.baseUrl("http://AUTH").defaultHeader("access", accessToken)
                .build().get().uri("/auth/passport").retrieve().bodyToMono(String.class);

        //passport를 header에 넣고 다음 필터로 이동
        return test.flatMap(responseData -> {
            exchange.getRequest().mutate().header("passport", responseData);

            return chain.filter(exchange);
        });
    }
}
