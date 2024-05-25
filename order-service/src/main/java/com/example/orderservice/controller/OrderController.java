package com.example.orderservice.controller;

import com.example.common.vo.ResultVO;
import com.example.orderservice.po.Order;
import com.example.orderservice.service.CartService;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.utils.DecodeUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final DecodeUtils decodeUtils;
    private final CartService cartService;
    private final ObjectMapper objectMapper;

    @GetMapping("/orders")
    public Mono<ResultVO> getOrders(ServerHttpRequest request) {
        return orderService.listOrders(decodeUtils.getUserId(request))
                .map(orderSkus -> ResultVO.success(Map.of("orders", orderSkus)));
    }

    @PostMapping("/orders")
    public Mono<ResultVO> postOrder(@RequestBody Order order) {
        return orderService.addOrder(order)
                .flatMap(savedOrder -> {
                    // Extract cart IDs from the orderSpec JSON string
                    List<Long> cartIds = extractCartIdsFromOrderSpec(savedOrder.getOrderSpec());
                    // Update cart status
                    return cartService.deleteByIds(cartIds)
                            .then(Mono.just(ResultVO.success(Map.of())));
                });
    }

    // Method to extract cart IDs from orderSpec JSON string
    private List<Long> extractCartIdsFromOrderSpec(String orderSpec) {
        if (orderSpec == null || orderSpec.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            List<Map<String, Object>> orderSpecList = objectMapper.readValue(orderSpec, new TypeReference<List<Map<String, Object>>>() {});
            List<Long> cartIds = new ArrayList<>();
            for (Map<String, Object> orderSpecItem : orderSpecList) {
                Object idObject = orderSpecItem.get("id");
                if (idObject != null) {
                    if (idObject instanceof Long) {
                        cartIds.add((Long) idObject);
                    } else if (idObject instanceof String) {
                        try {
                            cartIds.add(Long.parseLong((String) idObject));
                        } catch (NumberFormatException e) {
                            log.error("Error parsing cart ID: {}", e.getMessage());
                        }
                    }
                }
            }
            return cartIds;
        } catch (IOException e) {
            log.error("Error parsing orderSpec: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
