package com.example.product.repository;

import com.example.product.po.RdcSpu;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RdcSpuRepository extends ReactiveCrudRepository<RdcSpu, Long> {
    @Query("select * from rdc_spu r where r.rdc_id=:rid order by update_time desc limit :pageSize offset :offset")
    Flux<RdcSpu> findAll(long rid, int offset, int pageSize);

    @Query("select count(*) from rdc_spu r where r.rdc_id=:rid")
    Mono<Integer> findCount(long rid);
}
