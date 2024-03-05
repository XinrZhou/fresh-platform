package com.example.aigcservice.controller;

import com.example.aigcservice.po.Resource;
import com.example.aigcservice.service.ResourceService;
import com.example.common.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;

    @PostMapping("/resources")
    public Mono<ResultVO> postResource(@RequestBody Resource resource) {
        return resourceService.addResource(resource).map(resource1 -> ResultVO.success(Map.of()));
    }

    @GetMapping("/resources/{page}/{pageSize}/{type}")
    public Mono<ResultVO> getResources(@PathVariable int page, @PathVariable int pageSize, @PathVariable int type) {
        return resourceService.getCount(type)
                .flatMap(total -> resourceService.listResources(page, pageSize, type)
                        .map(resources -> ResultVO.success(Map.of("total", total, "resources", resources))));
    }

    @DeleteMapping("/resources/{rid}")
    private Mono<ResultVO> deleteResource(@PathVariable long rid) {
        return resourceService.deleteResource(rid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }
}
