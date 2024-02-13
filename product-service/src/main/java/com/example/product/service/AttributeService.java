package com.example.product.service;

import com.example.common.exception.XException;
import com.example.product.dto.AttributeDTO;
import com.example.product.po.Attribute;
import com.example.product.po.Category;
import com.example.product.repository.AttributeRepository;
import com.example.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttributeService {
    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;

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
                                        .build()))
                        .collectList());
    }

    public Mono<Void> deleteAttribute(long aid) {
        return attributeRepository.deleteById(aid).then();
    }
}
