package com.example.aigcservice.repository;

import com.example.aigcservice.po.Model;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ModelRepository extends ReactiveCrudRepository<Model, Long> {
    @Query("select * from model m where m.type=:type order by insert_time desc limit :pageSize offset :offset")
    Flux<Model> findByType(int offset, int pageSize, int type);

    @Query("select count(*) from model m where m.type=:type")
    Mono<Integer> findCount(int type);
}
