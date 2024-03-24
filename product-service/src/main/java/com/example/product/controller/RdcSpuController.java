package com.example.product.controller;

import com.example.common.vo.ResultVO;
import com.example.product.po.RdcSpu;
import com.example.product.service.RdcSpuService;
import com.example.product.service.SpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rdc_spu")
public class RdcSpuController {
    private final RdcSpuService rdcSpuService;

    @PostMapping("/rdc_spus")
    public Mono<ResultVO> addProducts(@RequestBody List<RdcSpu> rdcSpus) {
       return rdcSpuService.addRdcSpus(rdcSpus)
               .then(Mono.just(ResultVO.success(Map.of())));
    }

    @GetMapping("/rdc_spus/{rid}/{page}/{pageSize}")
    public Mono<ResultVO> getRdcSpus(@PathVariable long rid, @PathVariable int page, @PathVariable int pageSize) {
        return rdcSpuService.getRdcSpuCount(rid)
                .flatMap(total -> rdcSpuService.listRdcSpus(rid, page, pageSize)
                        .map(rdcSpus -> ResultVO.success(Map.of("rdcSpus", rdcSpus, "total", total))));
    }

    @DeleteMapping("/rdc_spus/{rid}")
    public Mono<ResultVO> deleteRdcSpu(@PathVariable long rid) {
        return rdcSpuService.deleteRdcSpu(rid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }
}
