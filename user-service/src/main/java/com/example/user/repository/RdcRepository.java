package com.example.user.repository;

import com.example.user.po.Rdc;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RdcRepository extends ReactiveCrudRepository<Rdc, Long> {
    @Query("select * from rdc order by update_time desc limit :pageSize offset :offset")
    Flux<Rdc> findAll(int offset, int pageSize);

    @Query("select count(*) from rdc")
    Mono<Integer> findCount();
}
