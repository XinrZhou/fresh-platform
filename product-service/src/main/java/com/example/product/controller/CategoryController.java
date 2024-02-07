package com.example.product.controller;

import com.example.common.vo.ResultVO;
import com.example.product.dto.CategoryDTO;
import com.example.product.po.Category;
import com.example.product.service.CategoryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
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

    @GetMapping("/categories")
    public Mono<ResultVO> getCategories() {
        return categoryService.listCategories()
                .map(categories -> ResultVO.success(Map.of("categories", categories)));
    }

    @GetMapping("/categories/tree")
    public Mono<ResultVO> getCategoriesTree() {
        return categoryService.listCategoryTree()
                .map(categoryDTOS -> ResultVO.success(Map.of("categories", categoryDTOS)));
    }

    @DeleteMapping("/category/{cid}")
    public Mono<ResultVO> deleteCategory(@PathVariable long cid) {
        return categoryService.deleteCategory(cid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }

}
