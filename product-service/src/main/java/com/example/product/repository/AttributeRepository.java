package com.example.product.repository;

import com.example.product.po.Attribute;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface AttributeRepository extends ReactiveCrudRepository<Attribute, Long> {
    Flux<Attribute> findByCategoryId(long cid);
}
