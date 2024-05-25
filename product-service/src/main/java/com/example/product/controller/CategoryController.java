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

    @GetMapping("/categories/{pid}/{page}/{pageSize}")
    public Mono<ResultVO> getCategories(@PathVariable long pid, @PathVariable int page, @PathVariable int pageSize) {
        Mono<Integer> categoriesCountM = categoryService.listCategoriesCount(pid);
        return categoriesCountM.flatMap(total -> categoryService.listCategories(pid, page, pageSize)
                .map(categories -> ResultVO.success(Map.of( "categories", categories, "total", total))));
    }

    @GetMapping("/categories/{level}")
    public Mono<ResultVO> getCategories(@PathVariable Integer level) {
        return categoryService.listCategories(level)
                .map(categories -> ResultVO.success(Map.of("categories", categories)));
    }

    @GetMapping("/categories/tree")
    public Mono<ResultVO> getCategoriesTree() {
        return categoryService.listCategoriesTree()
                .map(categoryDTOS -> ResultVO.success(Map.of("categories", categoryDTOS)));
    }

    @GetMapping("/categories/{page}/{pageSize}")
    public Mono<ResultVO> getCategories(@PathVariable int page,@PathVariable int pageSize) {
        Mono<Integer> categoriesCountM = categoryService.getCategoriesCount();
        return categoriesCountM.flatMap(total -> categoryService.listCategories(page, pageSize)
                .map(categoryDTOs -> ResultVO.success(Map.of( "categories", categoryDTOs, "total", total))));

    }

    @DeleteMapping("/categories/{cid}")
    public Mono<ResultVO> deleteCategory(@PathVariable long cid) {
        return categoryService.deleteCategory(cid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }


    @GetMapping("/category/{pid}")
    public Mono<ResultVO> getCategories(@PathVariable long pid) {
        return categoryService.listCategories(pid)
                .map(categories -> ResultVO.success(Map.of("categories", categories)));
    }
}
