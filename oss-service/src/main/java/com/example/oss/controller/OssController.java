package com.example.oss.controller;


import com.example.common.vo.ResultVO;
import com.example.oss.service.OssService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/oss")
public class OssController {
    private final OssService ossService;

    @PostMapping("/upload")
    public Mono<ResultVO> uploadFile(@RequestPart("file")FilePart file) throws IOException {
        String imageUrl = ossService.uploadFile(file);
        return Mono.just(ResultVO.success(Map.of("url", imageUrl)));
    }
}
