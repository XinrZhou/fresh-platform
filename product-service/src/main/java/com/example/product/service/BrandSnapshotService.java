package com.example.product.service;

import com.example.feignapi.client.UserClient;
import com.example.product.dto.BrandDTO;
import com.example.product.po.BrandSnapshot;
import com.example.product.repository.BrandSnapshotRepository;
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
public class BrandSnapshotService {
    private final BrandSnapshotRepository brandSnapshotRepository;
    private final CategoryRepository categoryRepository;
    private final UserClient userClient;

    public Mono<BrandSnapshot> addBrandSnapShot(BrandSnapshot brandSnapshot) {
        return brandSnapshotRepository.save(brandSnapshot);
    }

    public Mono<List<BrandDTO>> listBrandSnapshots() {
       return brandSnapshotRepository.findAll().collectList()
                .flatMapMany(Flux::fromIterable)
                .flatMap(brandSnapshot -> categoryRepository.findById(brandSnapshot.getCategoryId())
                        .zipWith(userClient.getInfo(brandSnapshot.getUserId()), (category, user) ->
                                BrandDTO.builder()
                                        .id(brandSnapshot.getId())
                                        .name(brandSnapshot.getName())
                                        .categoryId(brandSnapshot.getCategoryId())
                                        .categoryName(category.getName())
                                        .userName(user.getName())
                                        .status(brandSnapshot.getStatus())
                                        .insertTime(brandSnapshot.getInsertTime())
                                        .updateTime(brandSnapshot.getUpdateTime())
                                        .build()
                        )
                ).collectList();
    }
}
