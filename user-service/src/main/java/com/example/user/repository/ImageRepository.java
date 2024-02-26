package com.example.user.repository;

import com.example.user.po.Image;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ImageRepository extends ReactiveCrudRepository<Image, Long> {
    @Query("select * from image i where i.user_id=:uid order by update_time desc limit :pageSize offset :offset")
    Flux<Image> findByUserId(long uid, int offset, int pageSize);

    @Query("select count(*) from image i where i.user_id=:uid")
    Mono<Integer> findCount(long uid);
}
