package com.example.aigcservice.service;

import com.example.aigcservice.po.Resource;
import com.example.aigcservice.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;

    public Mono<Resource> addResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public Mono<List<Resource>> listResources(int page, int pageSize, int type) {
        return resourceRepository.findByType((page - 1) * pageSize, pageSize, type).collectList();
    }

    public Mono<Integer> getCount(int type) {
        return resourceRepository.findCount(type);
    }

    public Mono<Void> deleteResource(long rid) {
        return resourceRepository.deleteById(rid);
    }
}
