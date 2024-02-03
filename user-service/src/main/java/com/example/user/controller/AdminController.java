package com.example.user.controller;

import com.example.common.vo.ResultVO;
import com.example.user.po.User;
import com.example.user.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/business")
    public Mono<ResultVO> postBusiness(@RequestBody User user) {
        return adminService.addUser(user).map(r -> ResultVO.success(Map.of()));
    }
}
