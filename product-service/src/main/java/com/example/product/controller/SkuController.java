package com.example.product.controller;

import com.example.common.vo.ResultVO;
import com.example.product.po.Sku;
import com.example.product.service.SkuService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Api(tags = "商品Sku接口")
@RestController
@RequestMapping("/sku")
@RequiredArgsConstructor
public class SkuController {
    private final SkuService skuService;

    @PostMapping("/skus")
    private Mono<ResultVO> postSku(@RequestBody Sku sku) {
        return skuService.addSku(sku).map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/skus")
    private Mono<ResultVO> getSkus() {
        return skuService.listSkus()
                .map(skus -> ResultVO.success(Map.of("skus", skus)));
    }

    @DeleteMapping("/skus/{sid}")
    private Mono<ResultVO> deleteSku(@PathVariable long sid) {
        return skuService.deleteSku(sid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }
}
