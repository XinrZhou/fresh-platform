package com.example.configuration.service;

import com.example.configuration.po.MarketingActivity;
import com.example.configuration.po.MarketingAdvertisement;
import com.example.configuration.repository.MarketActivityRepository;
import com.example.configuration.repository.MarketingAdvertisementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MarketingAdvertisementService {
    private final MarketingAdvertisementRepository marketingAdvertisementRepository;

    public Mono<MarketingAdvertisement> addAdvertisement(MarketingAdvertisement advertisement) {
        return marketingAdvertisementRepository.save(advertisement);
    }

    public Mono<List<MarketingAdvertisement>> listAdvertisement() {
        return marketingAdvertisementRepository.findAll().collectList();
    }

    public Mono<Void> deleteAdvertisement(long aid) {
        return marketingAdvertisementRepository.deleteById(aid);
    }
}
