package com.example.product.service;

import com.example.product.dto.SkuDTO;
import com.example.product.po.Sku;
import com.example.product.po.SkuUser;
import com.example.product.repository.SkuRepository;
import com.example.product.repository.SkuUserRepository;
import com.example.product.repository.SpuRepository;
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
public class SkuService {
    private final SkuRepository skuRepository;
    private final SkuUserRepository skuUserRepository;
    private final SpuRepository spuRepository;

    @Transactional
    public Mono<SkuUser> addSku(Sku sku, long uid) {
        return skuRepository.save(sku)
                .flatMap(sku1 -> skuUserRepository.save(
                        SkuUser.builder()
                                .skuId(sku1.getId())
                                .userId(uid)
                                .build()
                ));
    }
    public Mono<List<SkuDTO>> listSkus(int page, int pageSize, long sid) {
        return skuRepository.findAll((page - 1) * pageSize, pageSize, sid)
                .collectList()
                .flatMap(this::mapSkusToDTO);
    }

    public Mono<List<SkuDTO>> listSkusByUserId(int page, int pageSize, long uid, long sid) {
        return skuUserRepository.findByUserId((page - 1) * pageSize, pageSize, uid)
                .flatMap(skuUser -> skuRepository.findById(skuUser.getSkuId()))
                .filter(sku -> sku.getSpuId() == sid)
                .collectList()
                .flatMap(this::mapSkusToDTO);
    }

    private Mono<List<SkuDTO>> mapSkusToDTO(List<Sku> skus) {
        return Flux.fromIterable(skus)
                .flatMap(sku -> spuRepository.findById(sku.getSpuId())
                        .map(spu -> SkuDTO.builder()
                                .id(sku.getId())
                                .name(sku.getName())
                                .spuId(sku.getSpuId())
                                .spuName(spu.getName())
                                .imageUrl(sku.getImageUrl())
                                .detailImageUrl(sku.getDetailImageUrl())
                                .unit(sku.getUnit())
                                .description(sku.getDescription())
                                .stock(sku.getStock())
                                .enable(sku.getEnable())
                                .originPrice(sku.getOriginPrice())
                                .discountPrice(sku.getDiscountPrice())
                                .build()))
                .collectList();
    }

    @Transactional
    public Mono<Void> deleteSku(long sid) {
        return skuRepository.deleteById(sid);
    }

    public Mono<List<SkuUser>> listSkuUsers(long uid) {
        return skuUserRepository.findByUserId(uid).collectList();
    }

    public Mono<Sku> getSku(long sid) {
        return skuRepository.findById(sid);
    }

    private Mono<SkuDTO> mapToSkuDTO(Sku sku) {
        return spuRepository.findById(sku.getSpuId())
                .map(spu -> SkuDTO.builder()
                        .id(sku.getId())
                        .spuName(spu.getName())
                        .name(sku.getName())
                        .imageUrl(sku.getImageUrl())
                        .detailImageUrl(sku.getDetailImageUrl())
                        .description(sku.getDescription())
                        .unit(sku.getUnit())
                        .originPrice(sku.getOriginPrice())
                        .discountPrice(sku.getDiscountPrice())
                        .tags(spu.getTags())
                        .genericSpec(spu.getGenericSpec())
                        .specialSpec(spu.getSpecialSpec())
                        .build());
    }

    public Mono<List<SkuDTO>> listSkus(int page, int pageSize) {
        return skuRepository.findAll((page - 1) * pageSize, pageSize)
                .flatMap(this::mapToSkuDTO)
                .collectList();
    }

    public Mono<List<SkuDTO>> listSkus(long cid) {
        return spuRepository.findByCategoryId(cid)
                .flatMap(spu -> skuRepository.findBySpuId(spu.getId())
                        .map(sku -> SkuDTO.builder()
                                .id(sku.getId())
                                .spuName(spu.getName())
                                .name(sku.getName())
                                .imageUrl(sku.getImageUrl())
                                .tags(spu.getTags())
                                .specialSpec(spu.getSpecialSpec())
                                .originPrice(sku.getOriginPrice())
                                .discountPrice(sku.getDiscountPrice())
                                .unit(sku.getUnit())
                                .build()))
                .collectList();
    }

    public Mono<List<Sku>> listSku(long sid) {
        return skuRepository.findById(sid)
                .flatMap(sku -> skuRepository.findBySpuId(sku.getSpuId())
                        .collectList());
    }

    public Mono<SkuDTO> getSkuDTO(long sid) {
        return skuRepository.findById(sid)
                .flatMap(this::mapToSkuDTO);
    }
}
