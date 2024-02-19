package com.example.product.controller;

import com.example.common.vo.ResultVO;
import com.example.product.po.RdcSpu;
import com.example.product.service.RdcSpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class RdcSpuController {
    private final RdcSpuService rdcSpuService;

    @PostMapping("/products")
    public Mono<ResultVO> addRdcSpus(@RequestBody List<RdcSpu> rdcSpus) {
       return rdcSpuService.addRdcSpus(rdcSpus)
               .map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/products")
    public Mono<ResultVO> getRdcSpus() {
        return rdcSpuService.listRdcSpus()
                .map(rdcSpus -> ResultVO.success(Map.of("rdcSpus", rdcSpus)));
    }
}
