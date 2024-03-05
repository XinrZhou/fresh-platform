package com.example.product.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.common.vo.RequestAttributeConstant;
import com.example.common.vo.ResultVO;

import com.example.feignapi.po.User;
import com.example.product.dto.SpuDTO;
import com.example.product.po.Spu;
import com.example.product.service.SpuService;
import com.example.product.utils.DecodeUtils;
import com.example.product.utils.JwtUtils;
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
    private Mono<ResultVO> postSpu(@RequestBody Spu spu, ServerHttpRequest request) {
        return spuService.addSpu(spu, decodeUtils.getUserId(request))
                .map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/spus/{page}/{pageSize}")
    private Mono<ResultVO> getSpus(@PathVariable int page, @PathVariable int pageSize, ServerHttpRequest request) {
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
