package com.example.product.service;

import com.example.product.dto.BrandDTO;
import com.example.product.po.Brand;
import com.example.product.repository.BrandRepository;
import com.example.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Mono<Brand> addBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public Mono<Integer> getBrandsCount() {
        return brandRepository.findCount();
    }
    public Mono<List<BrandDTO>> listBrands(int page, int pageSize) {
        return brandRepository.findAll((page - 1) * pageSize, pageSize).collectList()
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

    @Transactional
    public Mono<Void> deleteBrand(long bid) {
        return brandRepository.deleteById(bid).then();
    }
}
