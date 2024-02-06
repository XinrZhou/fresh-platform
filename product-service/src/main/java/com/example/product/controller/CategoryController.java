package com.example.product.controller;


import com.example.common.vo.ResultVO;
import com.example.product.po.Category;
import com.example.product.service.CategoryService;
import com.example.product.vo.CategoryVO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

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

    @GetMapping("/categories/{isPrent}")
    public Mono<ResultVO> getCategories(@PathVariable Integer isPrent) {
        return categoryService.listCategories()
                .map(categories -> ResultVO.success(Map.of("categories", categories)));
    }

    @GetMapping("/categories")
    public Mono<ResultVO> getCategories() {
        return categoryService.listCategories()
                .flatMap(categories -> Flux.fromIterable(categories)
                        .flatMap(category -> {
                            CategoryVO categoryVO = CategoryVO.builder()
                                    .id(category.getId())
                                    .name(category.getName())
                                    .updateTime(category.getUpdateTime())
                                    .imageUrl(category.getImageUrl())
                                    .isParent(category.getIsParent())
                                    .parentId(category.getParentId())
                                    .build();

                            if (category.getIsParent() != Category.PARENT) {
                                return categoryService.getCategory(category.getParentId())
                                        .filter(Objects::nonNull)
                                        .map(c -> {
                                            categoryVO.setParentName(c.getName());
                                            return c;
                                        })
                                        .thenReturn(categoryVO);
                            }

                            return Mono.just(categoryVO);
                        })
                        .collectList()
                        .map(categoryVOS -> ResultVO.success(Map.of("categories", categoryVOS)))
                );
    }

    @DeleteMapping("/category/{cid}")
    public Mono<ResultVO> deleteCategory(@PathVariable long cid) {
        return categoryService.deleteCategory(cid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }

}
