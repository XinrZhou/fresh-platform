package com.example.user.controller;


import com.example.common.exception.Code;
import com.example.common.vo.RequestAttributeConstant;
import com.example.common.vo.ResultVO;
import com.example.user.po.User;
import com.example.user.service.UserService;
import com.example.user.utils.JwtUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
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
        return userService.getUserByPhoneNumber(user.getPhoneNumber())
                .filter(u -> encoder.matches(user.getPassword(), u.getPassword()))
                .map(u -> {
                    Map<String, Object> tokenM = Map.of(RequestAttributeConstant.UID, u.getId(),
                            RequestAttributeConstant.ROLE, u.getRole());
                    String token = jwtUtils.encode(tokenM);
                    response.getHeaders().add(RequestAttributeConstant.TOKEN, token);

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

    @PostMapping("/register")
    public Mono<ResultVO> register(@RequestBody User user) {
        return userService.addUser(user).map(r -> ResultVO.success(Map.of()));
    }
}
