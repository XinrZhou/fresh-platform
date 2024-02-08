package com.example.product.controller;

import com.example.common.vo.ResultVO;
import com.example.product.po.Category;
import com.example.product.service.CategoryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Api(tags = "商品类目接口")
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor

public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    public Mono<ResultVO> postCategory(@RequestBody Category category) {
        return categoryService.addCategory(category).map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/category/{pid}")
    public Mono<ResultVO> getCategories(@PathVariable long pid) {
        return categoryService.listCategories(pid)
                .map(categories -> ResultVO.success(Map.of("categories", categories)));
    }

    @GetMapping("/categories/{level}")
    public Mono<ResultVO> getCategories(@PathVariable Integer level) {
        return categoryService.listCategories(level)
                .map(categories -> ResultVO.success(Map.of("categories", categories)));
//        return categoryService.listCategories(level)
//                .flatMap(categories -> Flux.fromIterable(categories)
//                        .flatMap(category -> {
//                            SelectOptionsVO selectOptionsVO = SelectOptionsVO.builder()
//                                    .value(category.getId())
//                                    .label(category.getName())
//                                    .build();
//                            return Mono.just(selectOptionsVO);
//                        })
//                        .collectList()
//                        .map(selectOptionsVOS -> ResultVO.success(Map.of("categories", selectOptionsVOS)))
//                );
    }

    @GetMapping("/categories")
    public Mono<ResultVO> getCategories() {
        return categoryService.listCategories()
                .map(categoryDTOS -> ResultVO.success(Map.of("categories", categoryDTOS)));
    }

    @DeleteMapping("/categories/{cid}")
    public Mono<ResultVO> deleteCategory(@PathVariable long cid) {
        return categoryService.deleteCategory(cid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }

}
