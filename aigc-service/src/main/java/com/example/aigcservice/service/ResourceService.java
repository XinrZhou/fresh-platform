package com.example.aigcservice.service;

import com.example.aigcservice.po.Resource;
import com.example.aigcservice.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;

    public Mono<Resource> addResource(Resource resource) {
        return resourceRepository.save(resource);
    }
}
