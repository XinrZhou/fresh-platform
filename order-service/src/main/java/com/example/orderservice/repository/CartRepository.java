package com.example.orderservice.repository;

import com.example.orderservice.po.Cart;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface CartRepository extends ReactiveCrudRepository<Cart, Long> {
    Flux<Cart> findByUserIdAndTypeOrderByUpdateTime(long uid, int type);

    @Modifying
    @Query("delete from cart where id IN (:cartIds)")
    Mono<Void> deleteByIdIn(List<Long> cartIds);
}
