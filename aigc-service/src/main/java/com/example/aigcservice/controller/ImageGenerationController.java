package com.example.aigcservice.controller;

import com.example.aigcservice.service.ImageGenerationService;
import com.example.common.vo.ResultVO;
import com.tencentcloudapi.aiart.v20221229.models.TextToImageRequest;
import com.tencentcloudapi.aiart.v20221229.models.TextToImageResponse;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ImageGenerationController {
    private final ImageGenerationService imageGenerationService;

    @PostMapping("/picture")
    public Mono<ResultVO> postAiPainting(@RequestBody TextToImageRequest textToImageRequest) throws TencentCloudSDKException {
        TextToImageResponse res = imageGenerationService.addAiPainting(textToImageRequest);
        return Mono.just(ResultVO.success(Map.of("images", res)));
    }
}
