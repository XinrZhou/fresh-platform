package com.example.configuration.repository;

import com.example.configuration.po.MarketingAdvertisement;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketingAdvertisementRepository extends ReactiveCrudRepository<MarketingAdvertisement, Long> {
}
