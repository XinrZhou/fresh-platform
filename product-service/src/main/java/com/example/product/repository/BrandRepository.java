package com.example.product.repository;

import com.example.product.po.Brand;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BrandRepository extends ReactiveCrudRepository<Brand, Long> {
    Flux<Brand> findByCategoryId(long cid);
}
