package com.example.aigcservice.controller;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.aigcservice.service.ImageGenerationService;
import com.example.aigcservice.service.TextGenerationService;
import com.example.common.vo.ResultVO;
import com.tencentcloudapi.aiart.v20221229.models.TextToImageRequest;
import com.tencentcloudapi.aiart.v20221229.models.TextToImageResponse;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.server.HttpServerResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ImageGenerationController {
    @Value("${my.ApiKey}")
    private String apiKey;
    private Generation generation;

    private final ImageGenerationService imageGenerationService;
    private final TextGenerationService textGenerationService;

    @PostMapping("/images")
    public Mono<ResultVO> postAiPainting(@RequestBody TextToImageRequest textToImageRequest) throws TencentCloudSDKException {
        TextToImageResponse res = imageGenerationService.addAiPainting(textToImageRequest);
        return Mono.just(ResultVO.success(Map.of("images", res)));
    }

    @PostMapping(value = "/text", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> aiTalk(@RequestBody String question, ServerHttpResponse response)
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
                    // GenerationResult对象中输出流(GenerationOutput)的choices是一个列表，存放着生成的数据。
                    String content = m.getOutput().getChoices().get(0).getMessage().getContent();
                    return ServerSentEvent.<String>builder().data(content).build();
                })
                .publishOn(Schedulers.boundedElastic())
                .doOnError(e -> {
                    Map<String, Object> map = new HashMap<>(){{
                        put("code", "400");
                        put("message", "出现了异常，请稍后重试");
                    }};
                    System.out.println(map);
                });
    }
}
