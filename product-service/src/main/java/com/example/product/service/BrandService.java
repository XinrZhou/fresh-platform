package com.example.product.service;

import com.example.product.dto.BrandDTO;
import com.example.product.po.Brand;
import com.example.product.repository.BrandRepository;
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
public class BrandService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public Mono<Brand> addBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public Mono<List<BrandDTO>> listBrands() {
        return brandRepository.findAll().collectList()
                .flatMap(brands ->  Flux.fromIterable(brands)
                        .flatMap(brand -> categoryRepository.findById(brand.getCategoryId())
                                .map(category -> BrandDTO.builder()
                                        .id(brand.getId())
                                        .name(brand.getName())
                                        .categoryId(brand.getCategoryId())
                                        .categoryName(category.getName())
                                        .status(brand.getStatus())
                                        .insertTime(brand.getInsertTime())
                                        .updateTime(brand.getUpdateTime())
                                        .build()))
                        .collectList());
    }

    public Mono<List<Brand>> listBrands(long cid) {
        return brandRepository.findByCategoryId(cid).collectList();
    }

    public Mono<Void> deleteBrand(long bid) {
        return brandRepository.deleteById(bid).then();
    }
}
