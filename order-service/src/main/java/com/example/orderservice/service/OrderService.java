package com.example.orderservice.service;

import com.example.feignapi.client.SkuClient;
import com.example.orderservice.po.OrderSku;
import com.example.orderservice.repository.OrderSkuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderSkuRepository orderSkuRepository;
    private final SkuClient skuClient;

    public Mono<List<OrderSku>> listOrders(long uid) {
        return skuClient.getSkus(uid)
                .flatMap(skuUsers -> Flux.fromIterable(skuUsers)
                        .flatMap(skuUser -> orderSkuRepository.findBySkuId(skuUser.getSkuId()))
                        .collectList());
    }
}
