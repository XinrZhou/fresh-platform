package com.example.user.repository;

import com.example.user.po.Rdc;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RdcRepository extends ReactiveCrudRepository<Rdc, Long> {
}
