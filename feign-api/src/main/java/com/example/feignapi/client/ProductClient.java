package com.example.feignapi.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("product-service")
public interface ProductClient {
}
