package com.example.aigcservice.repository;

import com.example.aigcservice.po.Resource;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ResourceRepository extends ReactiveCrudRepository<Resource, Long> {
    @Query("select * from resource r where r.type=:type order by insert_time desc limit :pageSize offset :offset")
    Flux<Resource> findByType(int offset, int pageSize, int type);

    @Query("select count(*) from model m where m.type=:type")
    Mono<Integer> findCount(int type);
}
