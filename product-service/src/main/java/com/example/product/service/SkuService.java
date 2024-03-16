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
//        Mono<Sku> saveSkuMono = skuRepository.save(sku);
//        Mono<SkuUser> saveSkuUserMono = saveSkuMono.flatMap(savedSku -> skuUserRepository.save(SkuUser.builder()
//                .userId(uid)
//                .skuId(savedSku.getId())
//                .build()));
//
//        if (sku.getIsDefault() == DEFAULT) {
//            return skuRepository.findBySpuId(sku.getSpuId())
//                    .flatMap(sku1 -> {
//                        sku1.setIsDefault(0);
//                        return skuRepository.save(sku1);
//                    })
//                    .then(saveSkuUserMono);
//        } else {
//            return saveSkuUserMono;
//        }
    }
    public Mono<List<SkuDTO>> listSkus(int page, int pageSize) {
        return skuRepository.findAll((page - 1) * pageSize, pageSize)
                .collectList()
                .flatMap(this::mapSkusToDTO);
    }

    public Mono<List<SkuDTO>> listSkusByUserId(int page, int pageSize, long uid) {
        return skuUserRepository.findByUserId((page - 1) * pageSize, pageSize, uid)
                .flatMap(skuUser -> skuRepository.findById(skuUser.getSkuId()))
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
}
