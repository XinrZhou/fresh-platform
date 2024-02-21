package com.example.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.common.vo.ResultVO;
import com.example.user.service.RdcService;
import com.example.user.vo.UserVO;
import com.example.user.po.User;
import com.example.user.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/supplier")
@Slf4j
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;
    private final RdcService rdcService;

    @PostMapping("/suppliers")
    public Mono<ResultVO> postSupplier(@RequestBody User user) {
        return supplierService.addUser(user).map(r -> ResultVO.success(Map.of()));
    }

    @GetMapping("/suppliers/{page}/{pageSize}")
    public Mono<ResultVO> getSuppliers(@PathVariable int page, @PathVariable int pageSize) {
        return supplierService.getSuppliersCount()
                .flatMap(total -> supplierService.listSuppliers(page, pageSize)
                        .flatMap(users -> Flux.fromIterable(users)
                                .flatMap(user -> {
                                    UserVO userVO = JSONObject.parseObject(user.getSupplier(), UserVO.class);
                                    userVO.setId(user.getId());
                                    userVO.setName(user.getName());
                                    userVO.setNumber(user.getNumber());
                                    userVO.setUpdateTime(user.getUpdateTime());

                                    return rdcService.getRdc(userVO.getRdcId())
                                            .map(rdc -> {
                                                userVO.setRdcName(rdc.getName());
                                                return rdc;
                                            })
                                            .thenReturn(userVO);
                                })
                                .collectList()
                                .map(suppliers -> ResultVO.success(Map.of("suppliers", suppliers, "total", total)))
                        ));
    }

    @DeleteMapping("/suppliers/{sid}")
    public Mono<ResultVO> deleteSupplier(@PathVariable long sid) {
        return supplierService.deleteSupplier(sid)
                .then(Mono.just(ResultVO.success(Map.of())));
    }
}

