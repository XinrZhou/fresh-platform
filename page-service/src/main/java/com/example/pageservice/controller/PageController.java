package com.example.pageservice.controller;

import com.example.common.vo.ResultVO;
import com.example.pageservice.po.Page;
import com.example.pageservice.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/page")
@RequiredArgsConstructor
public class PageController {
    private final PageService pageService;

    @PostMapping("/pages")
    public Mono<ResultVO> postPage(@RequestBody Page page) {
        return pageService.addPage(page)
                .map(page1 -> ResultVO.success(Map.of()));
    }

    @GetMapping("/pages/{name}")
    public Mono<ResultVO> getPage(@PathVariable String name) {
        return pageService.getPage(name)
                .map(page -> ResultVO.success(Map.of("pages", page)));
    }
}
