package com.example.user.service;

import com.example.user.po.Rdc;
import com.example.user.repository.RdcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RdcService {
    private final RdcRepository rdcRepository;

    public Mono<Rdc> addRdc(Rdc rdc) {
        return rdcRepository.save(rdc);
    }

    public Mono<List<Rdc>> listRdcs() {
        return rdcRepository.findAll().collectList();
    }
}
