package com.example.product.service;

import com.example.product.config.SnowFlakeGenerator;
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
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Mono<SpuUser> addSpu(Spu spu, long uid) {
        return spuRepository.save(spu)
                        .flatMap(spu1 -> spuUserRepository.save(SpuUser
                                .builder()
                                .userId(uid)
                                .spuId(spu1.getId())
                                .build())
                        );
    }
    public Mono<List<Spu>> listSpuOptions(long cid) {
        return spuRepository.findByCategoryId(cid).collectList()
                .flatMapMany(Flux::fromIterable)
                .map(spu -> Spu.builder()
                        .id(spu.getId())
                        .name(spu.getName())
                        .build())
                .collectList();
    }

    private Mono<SpuDTO> mapSpuToSpuDTO(Spu spu) {
        Mono<Category> categoryMono = categoryRepository.findById(spu.getCategoryId());
        Mono<Brand> brandMono = spu.getBrandId() != null ?
                brandRepository.findById(spu.getBrandId()) :
                Mono.just(Brand.builder().name("").build());

        return Mono.zip(categoryMono, brandMono)
                .map(tuple -> {
                    Category category = tuple.getT1();
                    Brand brand = tuple.getT2();
                    return SpuDTO.builder()
                            .id(spu.getId())
                            .name(spu.getName())
                            .categoryId(spu.getCategoryId())
                            .categoryName(category.getName())
                            .brandId(spu.getBrandId())
                            .brandName(brand.getName())
                            .imageUrl(spu.getImageUrl())
                            .genericSpec(spu.getGenericSpec())
                            .specialSpec(spu.getSpecialSpec())
                            .tags(spu.getTags())
                            .saleStatus(spu.getSaleStatus())
                            .build();
                });
    }

    public Mono<List<SpuDTO>> listSpus(int page, int pageSize) {
        return spuRepository.findAll((page - 1) * pageSize, pageSize)
                .flatMap(this::mapSpuToSpuDTO)
                .collectList();
    }

    public Mono<List<SpuDTO>> listSpusByUserId(int page, int pageSize, long uid) {
        return spuUserRepository.findByUserId((page - 1) * pageSize, pageSize, uid)
                .flatMap(spuUser -> spuRepository.findById(spuUser.getSpuId())
                        .flatMap(this::mapSpuToSpuDTO))
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

    @Transactional
    public Mono<Void> deleteSpu(long sid) {
        return spuRepository.deleteById(sid).then();
    }
}