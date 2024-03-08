package com.example.product.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.common.vo.ResultVO;
import com.example.product.dto.AttributeDTO;
import com.example.product.po.Attribute;
import com.example.product.service.AttributeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "属性接口")
@RestController
@RequestMapping("/attribute")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService attributeService;

    @PostMapping("/attributes")
    private Mono<ResultVO> postAttribute(@RequestBody AttributeDTO attributeDTO) {
        String categoryIds = attributeDTO.getCategoryIds();
        JSONArray jsonArray = JSONArray.parseArray(categoryIds);
        List<Long> categoryIdList = jsonArray.toJavaList(Long.class);

        List<Attribute> attributes = new ArrayList<>(categoryIdList.size());
        for (Long categoryId : categoryIdList) {
            attributes.add(Attribute.builder()
                    .name(attributeDTO.getName())
                    .categoryId(categoryId)
                    .value(attributeDTO.getValue())
                    .unit(attributeDTO.getUnit())
                    .isGeneric(attributeDTO.getIsGeneric())
                    .isNumeric(attributeDTO.getIsNumeric())
                    .build());
        }

        return attributeService.addAttribute(attributes)
                .map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/attributes/{page}/{pageSize}")
    private Mono<ResultVO> getAttributes(@PathVariable int page, @PathVariable int pageSize) {
        return attributeService.getAttributesCount()
                .flatMap(total -> attributeService.listAttributes(page, pageSize)
                        .map(attributes -> ResultVO.success(Map.of("attributes", attributes, "total", total))));
    }

    @GetMapping("/attributes/{cid}")
    private Mono<ResultVO> getAttributes(@PathVariable long cid) {
        return attributeService.listAttributes(cid)
                .map(attributes -> ResultVO.success(Map.of("attributes", attributes)));
    }

    @DeleteMapping("/attributes/{aid}")
    private Mono<ResultVO> deleteAttribute(@PathVariable long aid) {
        return attributeService.deleteAttribute(aid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }
}
