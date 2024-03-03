package com.example.aigcservice.controller;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.aigcservice.service.ImageGenerationService;
import com.example.common.exception.XException;
import com.example.common.vo.ResultVO;
import com.tencentcloudapi.aiart.v20221229.models.TextToImageRequest;
import com.tencentcloudapi.aiart.v20221229.models.TextToImageResponse;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AigcController {
    @Value("${my.ApiKey}")
    private String apiKey;

    private final Generation generation;
    private final ImageGenerationService imageGenerationService;

    @PostMapping("/images")
    public Mono<ResultVO> postAiPainting(@RequestBody TextToImageRequest textToImageRequest) throws TencentCloudSDKException {
        TextToImageResponse res = imageGenerationService.addAiPainting(textToImageRequest);
        return Mono.just(ResultVO.success(Map.of("images", res)));
    }


    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> aiTalk(@RequestBody String question )
            throws NoApiKeyException, InputRequiredException {
        Message message = Message.builder()
                .role(Role.USER.getValue())
                .content(question).build();

        QwenParam qwenParam = QwenParam.builder()
                .model(Generation.Models.QWEN_PLUS)
                .messages(Collections.singletonList(message))
                .topP(0.8)
                .resultFormat(QwenParam.ResultFormat.MESSAGE)
                .enableSearch(true)
                .apiKey(apiKey)
                .incrementalOutput(true)
                .build();
        Flowable<GenerationResult> result = generation.streamCall(qwenParam);

        return Flux.from(result)
                .map(m -> {
                    String content = m.getOutput().getChoices().get(0).getMessage().getContent();
                    return ServerSentEvent.<String>builder().data(content).build();
                })
                .publishOn(Schedulers.boundedElastic())
                .doOnError(e -> {
                    throw new XException(400, e.getMessage());
                });
    }
}
