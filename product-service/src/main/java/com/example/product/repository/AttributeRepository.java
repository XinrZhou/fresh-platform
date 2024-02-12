package com.example.product.repository;

import com.example.product.po.Attribute;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends ReactiveCrudRepository<Attribute, Long> {
}
