package com.example.product.repository;

import com.example.product.po.SkuUser;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SkuUserRepository extends ReactiveCrudRepository<SkuUser, Long> {
    @Query("select * from sku_user s where s.user_id=:uid order by update_time desc limit :pageSize offset :offset")
    Flux<SkuUser> findByUserId(int offset, int pageSize, long uid);

    Flux<SkuUser> findByUserId(long uid);
}
