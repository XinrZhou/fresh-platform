package com.example.product.repository;

import com.example.product.po.Sku;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkuRepository extends ReactiveCrudRepository<Sku, Long> {
}
