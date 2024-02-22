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

    @GetMapping("/snapshots/{page}/{pageSize}")
    public Mono<ResultVO> getBrandSnapshots(@PathVariable int page, @PathVariable int pageSize) {
        return brandSnapshotService.getBrandsSnapshotCount()
                .flatMap(total -> brandSnapshotService.listBrandSnapshots(page, pageSize)
                        .map(brandDTOS -> ResultVO.success(Map.of(
                                "brands", brandDTOS, "total", total
                        ))));
    }

    @GetMapping("/snapshots/{uid}/{page}/{pageSize}")
    public Mono<ResultVO> getBrandSnapshots(@PathVariable long uid, @PathVariable int page, @PathVariable int pageSize) {
        return brandSnapshotService.getBrandsSnapshotCount()
                .flatMap(total -> brandSnapshotService.listBrandSnapshots(page, pageSize, uid)
                        .map(brandDTOS -> ResultVO.success(Map.of(
                                "brands", brandDTOS, "total", total
                        ))));
    }

    @DeleteMapping("/snapshots/{bid}")
    public Mono<ResultVO> deleteBrandSnapshot(@PathVariable long bid) {
        return brandSnapshotService.deleteBrandSnapshot(bid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }

}
