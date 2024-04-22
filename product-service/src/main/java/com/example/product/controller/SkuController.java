package com.example.product.controller;

import com.example.common.vo.ResultVO;
import com.example.product.dto.SkuDTO;
import com.example.product.dto.SpuDTO;
import com.example.product.po.Sku;
import com.example.product.po.SkuUser;
import com.example.product.service.SkuService;
import com.example.product.utils.DecodeUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Api(tags = "商品Sku接口")
@RestController
@RequestMapping("/sku")
@RequiredArgsConstructor
public class SkuController {
    private final SkuService skuService;
    private final DecodeUtils decodeUtils;
    final int ADMIN_ROLE = 10;

    @PostMapping("/skus")
    private Mono<ResultVO> postSku(@RequestBody Sku sku, ServerHttpRequest request) {
        return skuService.addSku(sku, decodeUtils.getUserId(request))
                .map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/skus/{sid}/{page}/{pageSize}")
    private Mono<ResultVO> getSkus(@PathVariable int page, @PathVariable int pageSize, @PathVariable long sid,  ServerHttpRequest request) {
        long uid = decodeUtils.getUserId(request);
        int role = decodeUtils.getRole(request);

        Mono<List<SkuDTO>> skuListM;
        if (role == ADMIN_ROLE) {
            skuListM = skuService.listSkus(page, pageSize, sid);
        } else {
            skuListM = skuService.listSkusByUserId(page, pageSize, uid, sid);
        }
        return skuListM.map(skuDTOs -> ResultVO.success(Map.of("skus", skuDTOs)));
    }

    @DeleteMapping("/skus/{sid}")
    private Mono<ResultVO> deleteSku(@PathVariable long sid) {
        return skuService.deleteSku(sid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }

    @GetMapping("/order/{uid}")
    private Mono<List<SkuUser>> getSkus(@PathVariable long uid) {
        return skuService.listSkus(uid);
    }
}
