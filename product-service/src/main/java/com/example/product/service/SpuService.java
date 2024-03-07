package com.example.product.service;

import com.example.product.dto.RdcSpuDTO;
import com.example.product.dto.SpuDTO;
import com.example.product.po.Brand;
import com.example.product.po.Category;
import com.example.product.po.Spu;
import com.example.product.po.SpuUser;
import com.example.product.repository.BrandRepository;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.SpuRepository;
import com.example.product.repository.SpuUserRepository;
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
    private final SpuUserRepository spuUserRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public Mono<SpuUser> addSpu(Spu spu, long uid) {
        return spuRepository.save(spu)
                        .flatMap(spu1 -> spuUserRepository.save(SpuUser
                                .builder()
                                .userId(uid)
                                .spuId(spu1.getId())
                                .build())
                        );
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
                                                .detailImageUrl(spu.getDetailImageUrl())
                                                .saleStatus(spu.getSaleStatus())
                                                .build()
                                )
                )
                .collectList();
    }

    public Mono<List<SpuDTO>> listSpus(int page, int pageSize) {
        return spuRepository.findAll((page - 1) * pageSize, pageSize).collectList()
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
                                                .detailImageUrl(spu.getDetailImageUrl())
                                                .saleStatus(spu.getSaleStatus())
                                                .build()
                                )
                )
                .collectList();
    }

    public Mono<List<SpuDTO>> listSpusByUserId(int page, int pageSize, long uid) {
        return spuUserRepository.findByUserId((page - 1) * pageSize, pageSize, uid)
                .flatMap(spuUser -> spuRepository.findById(spuUser.getSpuId())
                        .flatMap(spu -> {
                            Mono<Category> categoryMono = categoryRepository.findById(spu.getCategoryId());
                            Mono<Brand> brandMono = brandRepository.findById(spu.getBrandId());

                            return Mono.zip(categoryMono, brandMono)
                                    .map(tuple -> {
                                        Category category = tuple.getT1();
                                        Brand brand = tuple.getT2();
                                        return SpuDTO.builder()
                                                .id(spu.getId())
                                                .name(spu.getName())
                                                .title(spu.getTitle())
                                                .categoryId(spu.getCategoryId())
                                                .categoryName(category.getName())
                                                .brandId(spu.getBrandId())
                                                .brandName(brand.getName())
                                                .imageUrl(spu.getImageUrl())
                                                .detailImageUrl(spu.getDetailImageUrl())
                                                .saleStatus(spu.getSaleStatus())
                                                .build();
                                    });
                        })
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