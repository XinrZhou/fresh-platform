package com.example.configuration.controller;

import com.example.common.vo.ResultVO;
import com.example.configuration.po.MarketingActivity;
import com.example.configuration.service.MarketingActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class MarketingActivityController {
    private final MarketingActivityService marketingActivityService;

    @PostMapping("/activities")
    public Mono<ResultVO> postActivity(@RequestBody MarketingActivity activity) {
        return marketingActivityService.addActivity(activity)
                .map(activity1 ->  ResultVO.success(Map.of()));
    }

    @GetMapping("/activities")
    public Mono<ResultVO> getActivities() {
        return marketingActivityService.listActivities()
                .map(marketingActivities -> ResultVO.success(Map.of("activities", marketingActivities)));
    }

    @DeleteMapping("/activities/{aid}")
    public Mono<ResultVO> deleteActivity(@PathVariable long aid) {
        return marketingActivityService.deleteActivity(aid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }
}
