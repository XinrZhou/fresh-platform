package com.example.orderservice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.common.exception.XException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${my.secretKey}")
    private String secretkey;

    public String encode(Map<String, Object> map) {
        LocalDateTime time = LocalDateTime.now().plusMonths(1);
        return JWT.create()
                .withPayload(map)
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(time.atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC256(secretkey));
    }

    public DecodedJWT decode(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secretkey)).build().verify(token);
        } catch (TokenExpiredException | SignatureVerificationException | JWTDecodeException e) {
            String msg = e instanceof TokenExpiredException
                    ? "过期请重新登录" : "无权限";
            throw new XException(403, msg);
        }
    }
}
