package com.example.configuration.service;

import com.example.configuration.po.MarketingActivity;
import com.example.configuration.repository.MarketActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MarketingActivityService {
    private final MarketActivityRepository marketActivityRepository;

    public Mono<MarketingActivity> addActivity(MarketingActivity activity) {
        return marketActivityRepository.save(activity);
    }

    public Mono<List<MarketingActivity>> listActivities() {
        return marketActivityRepository.findAll().collectList();
    }

    public Mono<Void> deleteActivity(long aid) {
        return marketActivityRepository.deleteById(aid);
    }
}
