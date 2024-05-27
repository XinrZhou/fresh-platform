package com.example.orderservice.repository;

import com.example.orderservice.po.SaleOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SaleOrderRepository extends ReactiveCrudRepository<SaleOrder, Long> {
}
