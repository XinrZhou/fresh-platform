package com.example.product.service;

import com.example.product.dto.SkuDTO;
import com.example.product.po.Sku;
import com.example.product.repository.SkuRepository;
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
public class SkuService {
    private final SkuRepository skuRepository;
    private final SpuRepository spuRepository;

    public Mono<Sku> addSku(Sku sku) {
        return skuRepository.save(sku);
    }

    public Mono<List<SkuDTO>> listSkus() {
        return skuRepository.findAll().collectList()
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
                                        .specialSpec(sku.getGenericSpec())
                                        .build()))
                        .collectList()
                );
    }

    public Mono<Void> deleteSku(long sid) {
        return skuRepository.deleteById(sid);
    }
}
