package com.example.product.controller;

import com.example.common.vo.ResultVO;
import com.example.product.po.Attribute;
import com.example.product.service.AttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/attribute")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService attributeService;

    @PostMapping("/attributes")
    private Mono<ResultVO> postAttribute(@RequestBody Attribute attribute) {
        return attributeService.addAttribute(attribute).map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/attributes")
    private Mono<ResultVO> getAttributes() {
        return attributeService.listAttributes()
                .map(attributes -> ResultVO.success(Map.of("attributes", attributes)));
    }
}
