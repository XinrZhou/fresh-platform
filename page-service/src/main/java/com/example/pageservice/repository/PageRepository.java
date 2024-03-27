package com.example.pageservice.repository;

import com.example.pageservice.po.Page;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PageRepository extends ReactiveCrudRepository<Page, Long> {
    Mono<Page> findFirstByName(String name);
}
