package com.example.orderservice.service;

import com.example.feignapi.client.ProductClient;
import com.example.orderservice.po.OrderSku;
import com.example.orderservice.po.SaleOrder;
import com.example.orderservice.repository.SaleOrderRepository;
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
public class SaleOrderService {
    private final SaleOrderRepository saleOrderRepository;
    private final OrderSkuRepository orderSkuRepository;
    private final ProductClient productClient;

    public Mono<List<OrderSku>> listOrders(long uid) {
        return productClient.getSkus(uid)
                .flatMap(skuUsers -> Flux.fromIterable(skuUsers)
                        .flatMap(skuUser -> orderSkuRepository.findBySkuId(skuUser.getSkuId()))
                        .collectList());
    }

    public Mono<SaleOrder> addOrder(SaleOrder saleOrder) {
        return saleOrderRepository.save(saleOrder);
    }
}
