package com.example.orderservice.repository;

import com.example.orderservice.po.OrderSku;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderSkuRepository extends ReactiveCrudRepository<OrderSku, Long> {
    Flux<OrderSku> findBySkuId(long sid);
}
