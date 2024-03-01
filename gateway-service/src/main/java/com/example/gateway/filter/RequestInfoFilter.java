package com.example.gateway.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.common.vo.RequestAttributeConstant;
import com.example.gateway.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Order(1)
@Slf4j
@RequiredArgsConstructor
public class RequestInfoFilter implements GlobalFilter {
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        String token = request.getHeaders().getFirst(RequestAttributeConstant.TOKEN);
        Long userId = getUserIdFromToken(token);

        if (userId != null) {
            incrementUserInterfaceCount(userId, path);
            if (exchange.getResponse().getStatusCode().isError()) {
                incrementFailedUserInterfaceCount(userId, path);
            }
        }

        return chain.filter(exchange)
                .doFinally(signalType -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("Request duration: {} ms", duration);
                });
    }

    private Long getUserIdFromToken(String token) {
        if (token != null) {
            DecodedJWT decode = jwtUtils.decode(token);
            return decode.getClaim(RequestAttributeConstant.UID).asLong();
        }
        return null;
    }

    private void incrementUserInterfaceCount(Long userId, String path) {
        String key = "user_interface_info:" + userId + ":" + path;
        reactiveRedisTemplate.opsForHash().increment(key, "success", 1)
                .then(reactiveRedisTemplate.expire(key, Duration.ofHours(24*30)))
                .subscribe();
    }

    private void incrementFailedUserInterfaceCount(Long userId, String path) {
        String key = "user_interface_info:" + userId + ":" + path;
        reactiveRedisTemplate.opsForHash().increment(key, "failure", 1)
                .then(reactiveRedisTemplate.expire(key, Duration.ofHours(24*30)))
                .subscribe();
    }
}

