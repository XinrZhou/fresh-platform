package com.example.aigcservice.service;

import com.example.aigcservice.po.Model;
import com.example.aigcservice.repository.ModelRepository;
import com.example.common.constant.StatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModelService {
    private final ModelRepository modelRepository;

    public Mono<List<Model>> listModels(int page, int pageSize, int type) {
        return modelRepository.findByType((page - 1) * pageSize, pageSize, type).collectList();
    }

    public Mono<Model> addModel(Model model) {
        return modelRepository.findByType(model.getType()).skip(1).next()
                .flatMap(firstModel -> {
                    firstModel.setStatus(StatusEnum.UNUSED.getCode());
                    return modelRepository.save(firstModel);
                })
                .then(modelRepository.save(model));
    }

    public Mono<Integer> getCount(int type) {
        return modelRepository.findCount(type);
    }

    public Mono<List<Model>> listModelParams() {
        return modelRepository.findByTypeAndStatus(Model.IMAGE, StatusEnum.IN_USE.getCode()).collectList();
    }
}
