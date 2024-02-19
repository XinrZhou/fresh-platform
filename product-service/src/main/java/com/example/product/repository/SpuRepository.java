package com.example.product.repository;

import com.example.product.po.Spu;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface SpuRepository extends ReactiveCrudRepository<Spu, Long> {
    Flux<Spu> findByCategoryIdIn(List<Long> cids);
}
