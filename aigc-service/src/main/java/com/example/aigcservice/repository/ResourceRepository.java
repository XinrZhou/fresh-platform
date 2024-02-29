package com.example.aigcservice.repository;

import com.example.aigcservice.po.Resource;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends ReactiveCrudRepository<Resource, Long> {
}
