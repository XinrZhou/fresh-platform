package com.example.configuration.controller;

import com.example.common.vo.ResultVO;
import com.example.configuration.po.MarketingAdvertisement;
import com.example.configuration.service.MarketingAdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/advertisement")
@RequiredArgsConstructor
public class MarketingAdvertisementController {
    private final MarketingAdvertisementService marketingAdvertisementService;

    @PostMapping("/advertisements")
    public Mono<ResultVO> postAdvertisement(@RequestBody MarketingAdvertisement advertisement) {
        return marketingAdvertisementService.addAdvertisement(advertisement)
                .map(advertisement1 ->  ResultVO.success(Map.of()));
    }

    @GetMapping("/advertisements")
    public Mono<ResultVO> getActivities() {
        return marketingAdvertisementService.listAdvertisement()
                .map(marketingAdvertisements -> ResultVO.success(Map.of("advertisements", marketingAdvertisements)));
    }

    @DeleteMapping("/advertisements/{aid}")
    public Mono<ResultVO> deleteAdvertisement(@PathVariable long aid) {
        return marketingAdvertisementService.deleteAdvertisement(aid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }
}
