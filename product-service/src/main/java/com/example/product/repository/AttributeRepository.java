package com.example.product.repository;

import com.example.product.po.Attribute;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface AttributeRepository extends ReactiveCrudRepository<Attribute, Long> {
    @Query("select * from attribute order by insert_time desc limit :pageSize offset :offset;")
    Flux<Attribute> findAll(int offset, int pageSize);

    @Query("select count(*) from attribute")
    Mono<Integer> findCount();

    Flux<Attribute> findByCategoryId(long cid);
}
