package com.example.product.repository;

import com.example.product.po.Brand;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BrandRepository extends ReactiveCrudRepository<Brand, Long> {
    @Query("select * from brand order by insert_time desc limit :pageSize offset :offset;")
    Flux<Brand> findAll(int offset, int pageSize);

    @Query("select count(*) from brand")
    Mono<Integer> findCount();

    Flux<Brand> findByCategoryId(long cid);
}
