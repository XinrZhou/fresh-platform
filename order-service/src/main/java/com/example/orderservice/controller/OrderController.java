package com.example.orderservice.controller;

import com.example.common.vo.ResultVO;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.utils.DecodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final DecodeUtils decodeUtils;

    @GetMapping("/orders")
    public Mono<ResultVO> getOrders(ServerHttpRequest request) {
        return orderService.listOrders(decodeUtils.getUserId(request))
                .map(orderSkus -> ResultVO.success(Map.of("orders", orderSkus)));
    }
}
