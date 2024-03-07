package com.example.product.service;

import com.example.product.dto.SkuDTO;
import com.example.product.dto.SpuDTO;
import com.example.product.po.*;
import com.example.product.repository.SkuRepository;
import com.example.product.repository.SkuUserRepository;
import com.example.product.repository.SpuRepository;
import com.example.product.repository.SpuUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SkuService {
    private final SkuRepository skuRepository;
    private final SpuRepository spuRepository;
    private final SkuUserRepository skuUserRepository;

    public Mono<SkuUser> addSku(Sku sku, long uid) {
        return skuRepository.save(sku)
                .flatMap(sku1 -> skuUserRepository.save(SkuUser
                        .builder()
                        .userId(uid)
                        .skuId(sku1.getId())
                        .build())
                );
    }

    public Mono<List<SkuDTO>> listSkus(int page, int pageSize) {
        return skuRepository.findAll((page - 1) * pageSize, pageSize).collectList()
                .flatMap(skus -> Flux.fromIterable(skus)
                        .flatMap(sku -> spuRepository.findById(sku.getSpuId())
                                .map(spu -> SkuDTO.builder()
                                        .id(sku.getId())
                                        .name(sku.getName())
                                        .spuId(sku.getSpuId())
                                        .spuName(spu.getName())
                                        .stock(sku.getStock())
                                        .enable(sku.getEnable())
                                        .originPrice(sku.getOriginPrice())
                                        .discountPrice(sku.getDiscountPrice())
                                        .genericSpec(sku.getGenericSpec())
                                        .specialSpec(sku.getSpecialSpec())
                                        .build()))
                        .collectList()
                );
    }

    public Mono<List<SkuDTO>> listSkusByUserId(int page, int pageSize, long uid) {
        return skuUserRepository.findByUserId((page - 1) * pageSize, pageSize, uid)
                .flatMap(skuUser -> skuRepository.findById(skuUser.getSkuId())
                        .flatMap(sku -> spuRepository.findById(sku.getSpuId())
                                .map(spu -> SkuDTO.builder()
                                        .id(sku.getId())
                                        .name(sku.getName())
                                        .spuId(sku.getSpuId())
                                        .spuName(spu.getName())
                                        .stock(sku.getStock())
                                        .enable(sku.getEnable())
                                        .originPrice(sku.getOriginPrice())
                                        .discountPrice(sku.getDiscountPrice())
                                        .genericSpec(sku.getGenericSpec())
                                        .specialSpec(sku.getSpecialSpec())
                                        .build()
                                )
                        )
                )
                .collectList();
    }

    public Mono<Void> deleteSku(long sid) {
        return skuRepository.deleteById(sid);
    }
}
