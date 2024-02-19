package com.example.product.service;

import com.example.product.dto.SpuDTO;
import com.example.product.po.Category;
import com.example.product.po.Spu;
import com.example.product.repository.BrandRepository;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.SpuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpuService {
    private final SpuRepository spuRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public Mono<Spu> addSpu(Spu spu) {
        return spuRepository.save(spu);
    }

    public Mono<List<SpuDTO>> listSpus() {
        return spuRepository.findAll().collectList()
                .flatMapMany(Flux::fromIterable)
                .flatMap(spu ->
                        categoryRepository.findById(spu.getCategoryId())
                                .zipWith(brandRepository.findById(spu.getBrandId()), (category, brand) ->
                                        SpuDTO.builder()
                                                .id(spu.getId())
                                                .name(spu.getName())
                                                .title(spu.getTitle())
                                                .categoryId(spu.getCategoryId())
                                                .categoryName(category.getName())
                                                .brandId(spu.getBrandId())
                                                .brandName(brand.getName())
                                                .imageUrl(spu.getImageUrl())
                                                .saleStatus(spu.getSaleStatus())
                                                .build()
                                )
                )
                .collectList();
    }

    public Mono<List<SpuDTO>> listSpus(long cid) {
        return categoryRepository.findAll().collectList()
                .flatMap(categories -> {
                    List<Long> subcategoryIds = new ArrayList<>();
                    collectSubcategoryIds(categories, cid, subcategoryIds);
                    return spuRepository.findByCategoryIdIn(subcategoryIds)
                            .map(spu -> SpuDTO.builder()
                                    .id(spu.getId())
                                    .name(spu.getName())
                                    .build())
                            .collectList();
                });
    }

    // 递归获取类目的所有子类目ID
    private void collectSubcategoryIds(List<Category> categories, long cid, List<Long> subcategoryIds) {
        for (Category category : categories) {
            if (category.getParentId() == cid) {
                subcategoryIds.add(category.getId());
                collectSubcategoryIds(categories, category.getId(), subcategoryIds);
            }
        }
    }

    public Mono<Void> deleteSpu(long sid) {
        return spuRepository.deleteById(sid).then();
    }
}