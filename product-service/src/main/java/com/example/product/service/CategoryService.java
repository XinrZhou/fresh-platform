package com.example.product.service;

import com.example.common.exception.XException;
import com.example.product.dto.CategoryDTO;
import com.example.product.po.Category;
import com.example.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Mono<Category> addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Mono<List<Category>> listCategories(Long pid) {
        return categoryRepository.findByParentId(pid).collectList();
    }

    public Mono<List<Category>> listCategories(Integer level) {
        return categoryRepository.findByLevel(level).collectList();
    }
    public Mono<List<CategoryDTO>> listCategories() {
        return categoryRepository.findAll()
                .filter(category -> category.getLevel() == Category.FIRST)
                .flatMap(this::mapCategoryToNode)
                .collectList();
    }

    private Mono<CategoryDTO> mapCategoryToNode(Category category) {
        return categoryRepository.findByParentId(category.getId())
                .flatMap(child -> mapCategoryToNode(child))
                .collectList()
                .map(children -> {
                    CategoryDTO categoryDTO = CategoryDTO.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .imageUrl(category.getImageUrl())
                            .parentId(category.getParentId())
                            .level(category.getLevel())
                            .status(category.getStatus())
                            .updateTime(category.getUpdateTime())
                            .children(children)
                            .build();
                    return categoryDTO;
                });
    }
    public Mono<Void> deleteCategory(long cid) {
        return categoryRepository.findCountByParentId(cid)
                .filter(c -> c == 0)
                .switchIfEmpty(Mono.error(new XException(XException.BAD_REQUEST, "存在子类目，无法删除")))
                .flatMap(c -> categoryRepository.deleteById(cid))
                .then();
    }
}
