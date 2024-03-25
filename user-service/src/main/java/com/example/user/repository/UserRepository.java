package com.example.user.repository;

import com.example.user.po.User;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Modifying
    @Query("update user u set u.password=:password where u.id=:uid")
    Mono<Integer> updatePassword(long uid, String password);

    Mono<User> findByNumber(String number);

    @Query("select * from user u where u.role=:role order by update_time desc limit :pageSize offset :offset")
    Flux<User> findByRole(int role, int offset, int pageSize);

    @Query("select count(*) from user u where u.role=:role")
    Mono<Integer> findCountByRole(int role);
}
