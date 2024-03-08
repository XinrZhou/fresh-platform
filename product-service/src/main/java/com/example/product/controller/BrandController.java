package com.example.product.controller;

import com.example.common.vo.ResultVO;
import com.example.product.po.Brand;
import com.example.product.service.BrandService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Api(tags = "品牌接口")
@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @PostMapping("/brands")
    public Mono<ResultVO> postBrand(@RequestBody Brand brand) {
        return brandService.addBrand(brand).map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/brands/{page}/{pageSize}")
    public Mono<ResultVO> getBrands(@PathVariable int page, @PathVariable int pageSize) {
        return brandService.getBrandsCount()
                .flatMap(total -> brandService.listBrands(page, pageSize)
                        .map(brands -> ResultVO.success(Map.of("brands", brands, "total", total))));
    }

    @GetMapping("/brands/{cid}")
    public Mono<ResultVO> getBrands(@PathVariable long cid) {
        return brandService.listBrands(cid)
                .map(brands -> ResultVO.success(Map.of("brands", brands)));
    }

    @DeleteMapping("/brands/{bid}")
    public Mono<ResultVO> deleteBrand(@PathVariable long bid) {
        return brandService.deleteBrand(bid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }
}
