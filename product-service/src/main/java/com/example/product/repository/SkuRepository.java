package com.example.product.repository;

import com.example.product.po.Sku;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SkuRepository extends ReactiveCrudRepository<Sku, Long> {
    @Query("select * from sku s where s.spu_id=:sid order by update_time desc limit :pageSize offset :offset")
    Flux<Sku> findAll(int offset, int pageSize, long sid);

    @Query("select * from sku s where s.user_id=:uid s.spu_id=:sid order by update_time desc limit :pageSize offset :offset")
    Flux<Sku> findByUserIdAndSpuId(int offset, int pageSize, long uid, long sid);
}
