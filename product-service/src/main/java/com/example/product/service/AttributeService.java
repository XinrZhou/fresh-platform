package com.example.product.service;

import com.alibaba.fastjson.JSONArray;
import com.example.common.exception.XException;
import com.example.product.dto.AttributeDTO;
import com.example.product.po.Attribute;
import com.example.product.po.Category;
import com.example.product.repository.AttributeRepository;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.SpuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttributeService {
    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Mono<List<Attribute>> addAttribute(List<Attribute> attributes) {
        return attributeRepository.saveAll(attributes).collectList();
    }

    public Mono<Integer> getAttributesCount() {
        return attributeRepository.findCount();
    }

    public Mono<List<AttributeDTO>> listAttributes(int page, int pageSize) {
        return attributeRepository.findAll((page - 1) * pageSize, pageSize).collectList()
                .flatMap(attributes -> Flux.fromIterable(attributes)
                        .flatMap(attribute -> categoryRepository.findById(attribute.getCategoryId())
                                .map(category -> AttributeDTO.builder()
                                        .id(attribute.getId())
                                        .name(attribute.getName())
                                        .categoryId(attribute.getCategoryId())
                                        .categoryName(category.getName())
                                        .unit(attribute.getUnit())
                                        .isNumeric(attribute.getIsNumeric())
                                        .isGeneric(attribute.getIsGeneric())
                                        .value(attribute.getValue())
                                        .build()))
                        .collectList());
    }

    public Mono<List<Attribute>> listAttributes(long cid) {
        return getAllAttributesByCategoryId(cid);
    }

    private Mono<List<Attribute>> getAllAttributesByCategoryId(long cid) {
        return categoryRepository.findById(cid)
                .flatMap(category -> {
                    Mono<List<Attribute>> currentAttributes = attributeRepository.findByCategoryId(cid).collectList();
                    if (category.getLevel() == Category.FIRST) {
                        // 无父类目，直接返回当前类目属性
                        return currentAttributes;
                    } else {
                        // 递归获取父类目属性，将当前类目属性与父类目属性合并
                        return getAllAttributesByCategoryId(category.getParentId())
                                .flatMap(parentAttributes -> currentAttributes.map(current -> {
                                    parentAttributes.addAll(current);
                                    return parentAttributes;
                                }));
                    }
                });
    }

    public Mono<Void> deleteAttribute(long aid) {
        return attributeRepository.deleteById(aid).then();
    }
}
