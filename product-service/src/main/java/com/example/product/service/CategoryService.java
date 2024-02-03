package com.example.product.service;

import com.example.common.exception.XException;
import com.example.product.po.Category;
import com.example.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Mono<Category> getCategory(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Mono<Category> addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Mono<List<Category>> listCategories(Integer isParent) {
        return categoryRepository.list(isParent).collectList();
    }

    public Mono<List<Category>> listCategories() {
        return categoryRepository.findAll().collectList();
    }

    public Mono<Void> deleteCategory(long cid) {
        return categoryRepository.findCountByParentId(cid)
                .filter(c -> c == 0)
                .switchIfEmpty(Mono.error(new XException(XException.BAD_REQUEST, "存在子类目，无法删除")))
                .flatMap(c -> categoryRepository.deleteById(cid))
                .then();
    }
}
