package com.example.orderservice.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.common.vo.RequestAttributeConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DecodeUtils {
    private final JwtUtils jwtUtils;

    public Long getUserId(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(RequestAttributeConstant.TOKEN);
        DecodedJWT decode = jwtUtils.decode(token);
        return decode.getClaim(RequestAttributeConstant.UID).asLong();
    }

    public Integer getRole(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(RequestAttributeConstant.TOKEN);
        DecodedJWT decode = jwtUtils.decode(token);
        return decode.getClaim(RequestAttributeConstant.ROLE).asInt();
    }
}
