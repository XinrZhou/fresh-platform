package com.example.product.service;

import com.example.product.po.RdcSpu;
import com.example.product.repository.RdcSpuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RdcSpuService {
    private final RdcSpuRepository rdcSpuRepository;

    @Transactional
    public Mono<Void> addRdcSpus(List<RdcSpu> rdcSpus) {
        return rdcSpuRepository.saveAll(rdcSpus).then();
    }


    public Mono<List<RdcSpu>> listRdcSpus() {
        return rdcSpuRepository.findAll().collectList();
    }

    public Mono<Void> deleteRdcSpus(long rid) {
        return rdcSpuRepository.deleteById(rid).then();
    }
}
