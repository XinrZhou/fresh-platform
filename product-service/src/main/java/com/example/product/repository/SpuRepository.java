package com.example.product.repository;

import com.example.product.po.Spu;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpuRepository extends ReactiveCrudRepository<Spu, Long> {
}
