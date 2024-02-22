package com.example.product.repository;

import com.example.product.po.BrandSnapshot;
import com.example.product.po.Category;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BrandSnapshotRepository extends ReactiveCrudRepository<BrandSnapshot, Long> {
    @Query("select * from brand_snapshot order by update_time desc limit :pageSize offset :offset")
    Flux<BrandSnapshot> findAll(int offset, int pageSize);

    @Query("select * from brand_snapshot b where b.user_id=:uid order by update_time desc limit :pageSize offset :offset")
    Flux<BrandSnapshot> findByUserId(int offset, int pageSize, long uid);

    @Query("select count(*) from brand_snapshot")
    Mono<Integer> findCount();
}
