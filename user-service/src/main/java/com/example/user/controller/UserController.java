package com.example.user.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.common.exception.Code;
import com.example.common.vo.RequestAttributeConstant;
import com.example.common.vo.ResultVO;
import com.example.user.po.User;
import com.example.user.service.UserService;
import com.example.user.utils.JwtUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public Mono<ResultVO> login(@RequestBody User user, ServerHttpResponse response) {
        return userService.getUser(user.getPhoneNumber())
                .filter(u -> encoder.matches(user.getPassword(), u.getPassword()))
                .map(u -> {
                    Map<String, Object> tokenM = Map.of(RequestAttributeConstant.UID, u.getId(),
                            RequestAttributeConstant.ROLE, u.getRole());
                    String token = jwtUtils.encode(tokenM);
                    response.getHeaders().add(RequestAttributeConstant.TOKEN, token);
                    response.getHeaders().add(RequestAttributeConstant.UID, u.getId().toString());

                    String role = switch (u.getRole()) {
                        case User.ADMIN -> "Vo10t";
                        case User.BUSINESS -> "cA1KL";
                        case User.CONSUMER -> "sfYaT";
                        default -> "";
                    };
                    response.getHeaders().add(RequestAttributeConstant.ROLE, role);
                    return ResultVO.success(Map.of());
                })
                .defaultIfEmpty(ResultVO.error(Code.LOGIN_ERROR));
    }

    @GetMapping("/info")
    public Mono<ResultVO> getInfo(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(RequestAttributeConstant.TOKEN);
        DecodedJWT decode = jwtUtils.decode(token);
        return userService.getUser(decode.getClaim(RequestAttributeConstant.UID).asLong())
                .map(user -> ResultVO.success(Map.of("user", user)));
    }


}
