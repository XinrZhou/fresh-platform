package com.example.product.service;

import com.example.product.dto.SpuDTO;
import com.example.product.po.Spu;
import com.example.product.repository.BrandRepository;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.SpuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
                .flatMap(spus -> Flux.fromIterable(spus)
                        .flatMap(spu -> categoryRepository.findById(spu.getCategoryId())
                                .map(category -> SpuDTO.builder()
                                        .id(spu.getId())
                                        .name(spu.getName())
                                        .title(spu.getTitle())
                                        .categoryId(spu.getCategoryId())
                                        .categoryName(category.getName())
                                        .brandId(spu.getBrandId())
                                        .imageUrl(spu.getImageUrl())
                                        .build())
                                .flatMap(spuDTO -> brandRepository.findById(spu.getBrandId())
                                        .map(brand -> {
                                            spuDTO.setBrandName(brand.getName());
                                            return spuDTO;
                                        }))
                        )
                        .collectList());
    }

    public Mono<Void> deleteSpu(long sid) {
        return spuRepository.deleteById(sid).then();
    }
}