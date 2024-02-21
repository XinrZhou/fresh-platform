package com.example.user.repository;

import com.example.user.po.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("select * from user u where u.phone=:phone;")
    Mono<User> find(String phone);

    Mono<User> findByNumber(String phoneNumber);

    @Query("select * from user u where u.role=:role order by update_time desc limit :pageSize offset :offset")
    Flux<User> findByRole(int role, int offset, int pageSize);

    @Query("select count(*) from user u where u.role=:role")
    Mono<Integer> findCountByRole(int role);
}
