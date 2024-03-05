package com.example.product.repository;

import com.example.product.po.SpuUser;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SpuUserRepository extends ReactiveCrudRepository<SpuUser, Long> {

    @Query("select * from spu_user s where s.user_id=:uid order by update_time desc limit :pageSize offset :offset")
    Flux<SpuUser> findByUserId(int offset, int pageSize, long uid);
}
