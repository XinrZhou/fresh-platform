package com.example.product.controller;

import com.example.common.vo.ResultVO;
import com.example.product.po.Brand;
import com.example.product.po.BrandSnapshot;
import com.example.product.service.BrandSnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brand")
public class BrandSnapshotController {
    private final BrandSnapshotService brandSnapshotService;

    @PostMapping("/snapshots")
    public Mono<ResultVO> postBrandSnapshot(@RequestBody BrandSnapshot brandSnapshot) {
        return brandSnapshotService.addBrandSnapShot(brandSnapshot)
                .map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/snapshots")
    public Mono<ResultVO> getBrandSnapshots() {
        return brandSnapshotService.listBrandSnapshots()
                .map(brands -> ResultVO.success(Map.of("brands", brands)));
    }

//    @GetMapping("/brands")
//    public Mono<ResultVO> getBrands() {
//        return brandService.listBrands()
//                .map(brands -> ResultVO.success(Map.of("brands", brands)));
//    }
//
//    @GetMapping("/brands/{cid}")
//    public Mono<ResultVO> getBrands(@PathVariable long cid) {
//        return brandService.listBrands(cid)
//                .map(brands -> ResultVO.success(Map.of("brands", brands)));
//    }
//
//    @DeleteMapping("/brands/{bid}")
//    public Mono<ResultVO> deleteBrand(@PathVariable long bid) {
//        return brandService.deleteBrand(bid)
//                .then(Mono.just(ResultVO.success(Map.of())));
//    }

}
