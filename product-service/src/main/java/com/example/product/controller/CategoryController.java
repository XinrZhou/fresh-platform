package com.example.product.controller;


import com.example.common.vo.ResultVO;
import com.example.product.po.Category;
import com.example.product.service.CategoryService;
import com.example.product.dto.CategoryDTO;
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

    @PostMapping("/category")
    public Mono<ResultVO> postCategory(@RequestBody Category category) {
        return categoryService.addCategory(category).map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/categories/{isPrent}")
    public Mono<ResultVO> getCategories(@PathVariable Integer isPrent) {
        return categoryService.listCategories(isPrent)
                .map(categories -> ResultVO.success(Map.of("categories", categories)));
    }

    @GetMapping("/categories")
    public Mono<ResultVO> getCategories() {
        return categoryService.listCategories()
                .flatMap(categories -> Flux.fromIterable(categories)
                        .flatMap(category -> {
                            CategoryDTO categoryDTO = new CategoryDTO();

                            categoryDTO.setId(category.getId());
                            categoryDTO.setCategoryName(category.getCategoryName());
                            categoryDTO.setUpdateTime(category.getUpdateTime());
                            categoryDTO.setImageUrl(category.getImageUrl());
                            categoryDTO.setIsParent(category.getIsParent());

                            if (category.getIsParent() != Category.PARENT) {
                                return categoryService.getCategory(category.getParentId())
                                        .filter(Objects::nonNull)
                                        .map(c -> {
                                            categoryDTO.setParentName(c.getCategoryName());
                                            categoryDTO.setParentId(c.getParentId());
                                            return c;
                                        })
                                        .thenReturn(categoryDTO);
                            }

                            return Mono.just(categoryDTO);
                        })
                        .collectList()
                        .map(categoryDTOList -> ResultVO.success(Map.of("categories", categoryDTOList)))
                );
    }

    @DeleteMapping("/category/{cid}")
    public Mono<ResultVO> deleteCategory(@PathVariable long cid) {
        return categoryService.deleteCategory(cid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }

}
