package com.example.product.repository;

import com.example.product.po.RdcSpu;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RdcSpuRepository extends ReactiveCrudRepository<RdcSpu, Long> {

}
