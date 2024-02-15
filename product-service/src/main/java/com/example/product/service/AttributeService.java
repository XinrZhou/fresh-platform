package com.example.product.service;

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
    private final SpuRepository spuRepository;

    public Mono<Attribute> addAttribute(Attribute attribute) {
        return attributeRepository.save(attribute);
    }

    public Mono<List<AttributeDTO>> listAttributes() {
        return attributeRepository.findAll().collectList()
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

    public Mono<List<Attribute>> listAttributes(long sid) {
        return spuRepository.findById(sid)
                .flatMap(spu -> categoryRepository.findById(spu.getCategoryId())
                        .flatMap(category -> getAllAttributesByCategoryId(category.getId())));
    }

    private Mono<List<Attribute>> getAllAttributesByCategoryId(long cid) {
        return attributeRepository.findByCategoryId(cid)
                .collectList()
                .flatMap(attributes -> {
                    if (!attributes.isEmpty()) {
                        return Mono.just(attributes);
                    } else {
                        return categoryRepository.findById(cid)
                                .flatMap(category -> {
                                    if (category.getLevel().equals(Category.FIRST)) {
                                        return Mono.just(Collections.emptyList()); // 没有父类目，直接返回空列表
                                    } else {
                                        return getAllAttributesByCategoryId(category.getParentId());
                                    }
                                });
                    }
                });
    }

    public Mono<Void> deleteAttribute(long aid) {
        return attributeRepository.deleteById(aid).then();
    }
}
