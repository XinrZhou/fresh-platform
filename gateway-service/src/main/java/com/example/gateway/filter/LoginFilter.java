package com.example.gateway.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.common.exception.XException;
import com.example.gateway.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@Order(1)
@RequiredArgsConstructor
public class LoginFilter implements WebFilter {

    private final JwtUtils jwtUtils;

    private final List<String> excludes = List.of("/api/login");
    private final List<String> includes = List.of("/api/");
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        for (String p : includes) {
            if (!exchange.getRequest().getPath().pathWithinApplication().value().startsWith(p)) {
                return chain.filter(exchange);
            }
        }
        for (String excludeP : excludes) {
            if (request.getPath().pathWithinApplication().value().equals(excludeP)) {
                return chain.filter(exchange);
            }
        }
        String token = request.getHeaders().getFirst("authorization");
        if (token == null) {
           throw new XException(401, "未登录");
        }

        DecodedJWT decode = jwtUtils.decode(token);
        exchange.getAttributes().put("uid", decode.getClaim("uid").asLong());
        exchange.getAttributes().put("role", decode.getClaim("role").asInt());
        return chain.filter(exchange);
    }
}
