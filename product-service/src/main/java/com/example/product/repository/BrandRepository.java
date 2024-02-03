package com.example.product.repository;

import com.example.product.po.Brand;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends ReactiveCrudRepository<Brand, Long> {
}
