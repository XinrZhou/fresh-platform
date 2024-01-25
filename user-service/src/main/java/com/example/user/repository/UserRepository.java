package com.example.user.repository;

import com.example.user.po.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("select * from user u where u.phone=:phone;")
    Mono<User> find(String phone);

    Mono<User> findByPhoneNumber(String phoneNumber);
}
