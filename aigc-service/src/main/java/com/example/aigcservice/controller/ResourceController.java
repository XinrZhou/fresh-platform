package com.example.aigcservice.controller;

import com.example.common.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private static String VALUE_TYPE = "hash";
    @GetMapping("/resources")
    public Mono<ResultVO> getResources() {
        return reactiveRedisTemplate.keys("*")
                .flatMap(key -> reactiveRedisTemplate.type(key)
                        .map(type -> Map.entry(key, type)))
                .filter(stringDataTypeEntry -> stringDataTypeEntry.getKey().split(":")[2].startsWith("/ai"))
                .flatMap(entry -> {
                    String key = entry.getKey();
                    String type = entry.getValue().code(); // 获取数据类型的字符串表示
                    String[] keyParts = key.split(":");
                    String userId = keyParts[1];
                    String path = keyParts[2];
                    return getInterfaceInfo(userId, path, type);
                })
                .collectList()
                .map(data -> ResultVO.success(Map.of("resources", data)));
    }

    private Mono<Map<String, Object>> getInterfaceInfo(String userId, String path, String type) {
        Map<String, Object> interfaceInfo = new HashMap<>();
        interfaceInfo.put("userId", userId);
        interfaceInfo.put("path", path);

        if (type.equals(VALUE_TYPE)) {
            String key = "user_interface_info:" + userId + ":" + path;
            ReactiveHashOperations<String, String, String> hashOps = reactiveRedisTemplate.opsForHash();
            return hashOps.entries(key)
                    .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                    .map(values -> {
                        interfaceInfo.put("values", values);
                        return interfaceInfo;
                    })
                    .doOnSuccess(info -> {
                        // Log values retrieved from Redis hash
                        System.out.println("Values for key " + key + ": " + info.get("values"));
                    });
        } else {
            return Mono.just(interfaceInfo);
        }
    }
}

