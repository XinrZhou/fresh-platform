package com.example.aigcservice.controller;

import com.example.aigcservice.po.Model;
import com.example.aigcservice.service.ModelService;
import com.example.common.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/model")
@RequiredArgsConstructor
public class ModelController {
    private final ModelService modelService;

    @GetMapping("/models/{page}/{pageSize}/{type}")
    public Mono<ResultVO> getModels(@PathVariable int page, @PathVariable int pageSize, @PathVariable int type) {
        return modelService.getCount(type)
                .flatMap(total -> modelService.listModels(page, pageSize, type)
                        .map(models -> ResultVO.success(Map.of("total", total, "models", models))));
    }

    @PostMapping("/models")
    public Mono<ResultVO> postModel(@RequestBody Model model) {
        return modelService.addModel(model).map(model1 -> ResultVO.success(Map.of()));
    }

    @GetMapping("/models")
    Mono<ResultVO> getModelParams() {
        return modelService.listModelParams()
                .map(models -> ResultVO.success(Map.of("models", models)));
    }
}
