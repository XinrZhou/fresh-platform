package com.example.feignapi.client;

import com.example.feignapi.config.CustomizedConfig;
import com.example.feignapi.po.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;


@ReactiveFeignClient(name = "user-service", configuration = CustomizedConfig.class)
public interface UserClient {
    @GetMapping("/users/info/{uid}")
    Mono<User> getInfo(@PathVariable long uid);
}
