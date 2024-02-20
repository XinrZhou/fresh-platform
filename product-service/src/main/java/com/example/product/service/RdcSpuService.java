package com.example.product.service;

import com.example.product.config.SnowFlakeGenerator;
import com.example.product.dto.BrandDTO;
import com.example.product.dto.RdcSpuDTO;
import com.example.product.dto.SpuDTO;
import com.example.product.po.RdcSpu;
import com.example.product.repository.RdcSpuRepository;
import com.example.product.repository.SpuRepository;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RdcSpuService {
    private final RdcSpuRepository rdcSpuRepository;
    private final SpuRepository spuRepository;
    private final ConnectionFactory connectionFactory;
    private final SnowFlakeGenerator snowFlakeGenerator;

    @Transactional
    public Mono<Void> addRdcSpus(List<RdcSpu> rdcSpus) {
        String sql = """
                insert ignore into rdc_spu(id, rdc_id, spu_id) values (?, ?, ?)
                """;
        return DatabaseClient.create(connectionFactory).sql(sql).filter(statement -> {
            for (RdcSpu r : rdcSpus) {
                statement.bind(0, snowFlakeGenerator.getNextId())
                        .bind(1, r.getRdcId())
                        .bind(2, r.getSpuId())
                        .add();
            }
            return statement;
        }).then();
    }

    public Mono<List<RdcSpuDTO>> listRdcSpus() {
        return rdcSpuRepository.findAll().collectList()
                .flatMapMany(Flux::fromIterable)
                .flatMap(rdcSpu -> spuRepository.findById(rdcSpu.getSpuId())
                        .map(spu -> RdcSpuDTO.builder()
                                .id(rdcSpu.getId())
                                .spuId(rdcSpu.getSpuId())
                                .spuName(spu.getName())
                                .rdcId(rdcSpu.getId())
                                .build()))
                .collectList();
    }

    public Mono<Void> deleteRdcSpu(long rid) {
        return rdcSpuRepository.deleteById(rid).then();
    }
}
