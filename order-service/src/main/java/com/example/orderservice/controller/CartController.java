package com.example.orderservice.controller;

import com.example.common.vo.ResultVO;
import com.example.orderservice.po.Cart;
import com.example.orderservice.service.CartService;
import com.example.orderservice.utils.DecodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/cart")
@Slf4j
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final DecodeUtils decodeUtils;

    @PostMapping("/carts")
    public Mono<ResultVO> postCart(@RequestBody Cart cart) {
        return cartService.addCart(cart).map(cart1 -> ResultVO.success(Map.of()));
    }

    @GetMapping("/carts")
    public Mono<ResultVO> getCarts(ServerHttpRequest request) {
        return cartService.listCarts(decodeUtils.getUserId(request))
                .map(carts -> ResultVO.success(Map.of("carts", carts)));
    }
}
