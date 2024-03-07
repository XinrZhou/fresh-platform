package com.example.product.controller;

import com.example.common.vo.ResultVO;

import com.example.product.dto.SpuDTO;
import com.example.product.po.Spu;
import com.example.product.service.SpuService;
import com.example.product.utils.DecodeUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Api(tags = "商品Spu接口")
@RestController
@RequestMapping("/spu")
@RequiredArgsConstructor
public class SpuController {
    private final SpuService spuService;
    private final DecodeUtils decodeUtils;
    final int ADMIN_ROLE = 10;

    @PostMapping("/spus")
    public Mono<ResultVO> postSpu(@RequestBody Spu spu, ServerHttpRequest request) {
        return spuService.addSpu(spu, decodeUtils.getUserId(request))
                .map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/spus")
    public Mono<ResultVO> getSpus() {
        return spuService.listSpus()
                .map(spuDTOS -> ResultVO.success(Map.of("spus", spuDTOS)));
    }


    @GetMapping("/spus/{page}/{pageSize}")
    public Mono<ResultVO> getSpus(@PathVariable int page, @PathVariable int pageSize, ServerHttpRequest request) {
        long uid = decodeUtils.getUserId(request);
        int role = decodeUtils.getRole(request);

        Mono<List<SpuDTO>> spuListM;
        if (role == ADMIN_ROLE) {
            spuListM = spuService.listSpus(page, pageSize);
        } else {
            spuListM = spuService.listSpusByUserId(page, pageSize, uid);
        }

        return spuListM.map(spus -> ResultVO.success(Map.of("spus", spus)));
    }

    @GetMapping("/spus/{cid}")
    public Mono<ResultVO> getSpus(@PathVariable long cid) {
        return spuService.listSpus(cid)
                .map(spuDTOS -> ResultVO.success(Map.of("spus", spuDTOS)));
    }

    @DeleteMapping("/spus/{sid}")
    public Mono<ResultVO> deleteSpu(@PathVariable long sid) {
        return spuService.deleteSpu(sid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }
}
