package com.example.configuration.repository;

import com.example.configuration.po.MarketingActivity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketActivityRepository extends ReactiveCrudRepository<MarketingActivity, Long> {
}
