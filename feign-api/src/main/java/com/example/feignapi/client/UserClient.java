package com.example.feignapi.client;

import com.example.feignapi.config.FeignConfig;
import com.example.feignapi.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@FeignClient(value = "user-service", configuration = FeignConfig.class)
public interface UserClient {
    @GetMapping("/users/info/{uid}")
    Mono<User> getInfo(@PathVariable long uid);
}
