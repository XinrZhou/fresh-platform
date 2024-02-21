package com.example.product.controller;

import com.example.common.vo.ResultVO;
import com.example.product.po.Spu;
import com.example.product.service.SpuService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Api(tags = "商品Spu接口")
@RestController
@RequestMapping("/spu")
@RequiredArgsConstructor
public class SpuController {
    private final SpuService spuService;

    @PostMapping("/spus")
    private Mono<ResultVO> postSpu(@RequestBody Spu spu) {
        return spuService.addSpu(spu).map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/spus")
    private Mono<ResultVO> getSpus() {
        return spuService.listSpus()
                .map(spus -> ResultVO.success(Map.of("spus", spus)));
    }

    @GetMapping("/spus/{cid}")
    private Mono<ResultVO> getSpus(@PathVariable long cid) {
        return spuService.listSpus(cid)
                .map(spuDTOS -> ResultVO.success(Map.of("spus", spuDTOS)));
    }

    @DeleteMapping("/spus/{sid}")
    private Mono<ResultVO> deleteSpu(@PathVariable long sid) {
        return spuService.deleteSpu(sid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }
}
