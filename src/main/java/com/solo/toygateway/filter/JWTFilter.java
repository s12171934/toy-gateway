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

        //Access token을 검증하는 과정, 검증 실패시 다음 필터로 진행하지 않음

        // 토큰 만료 여부 확인
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

        //Access Token 검증 완료

        //passport를 발급하고 request header에 넣는 과정
        //Global Filter가 비동기 interface로 Mono -> flatmap을 통해 request를 다음 Filter로 전달

        //passport 발급
        Mono<String> passport = webClientBuilder.baseUrl("http://AUTH").defaultHeader("access", accessToken)
                .build().get().uri("/auth/passport").retrieve().bodyToMono(String.class);

        //passport를 request header에 넣고 다음 필터로 이동
        return passport.flatMap(responseData -> {
            exchange.getRequest().mutate().header("passport", responseData);

            return chain.filter(exchange);
        });
    }
}
