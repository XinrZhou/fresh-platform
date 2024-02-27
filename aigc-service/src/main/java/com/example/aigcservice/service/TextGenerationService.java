package com.example.aigcservice.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.Semaphore;


@Service
@Slf4j
public class TextGenerationService {
    @Value("${my.ApiKey}")
    private String apiKey;

    public static void streamCallWithMessage()
            throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        Message userMsg = Message
                .builder()
                .role(Role.USER.getValue())
                .content("如何做西红柿炖牛腩？")
                .build();
        QwenParam param =
                QwenParam.builder().model(Generation.Models.QWEN_PLUS).messages(Arrays.asList(userMsg))
                        .resultFormat(QwenParam.ResultFormat.MESSAGE)
                        .topP(0.8)
                        .enableSearch(true)
                        .incrementalOutput(true) // get streaming output incrementally
                        .build();
        Flowable<GenerationResult> result = gen.streamCall(param);
        StringBuilder fullContent = new StringBuilder();
        result.blockingForEach(message -> {
            fullContent.append(message.getOutput().getChoices().get(0).getMessage().getContent());
            System.out.println(JsonUtils.toJson(message));
        });
        System.out.println("Full content: \n" + fullContent.toString());
    }

    public static void streamCallWithCallback()
            throws NoApiKeyException, ApiException, InputRequiredException,InterruptedException {
        Generation gen = new Generation();
        Message userMsg = Message
                .builder()
                .role(Role.USER.getValue())
                .content("如何做西红柿炖牛腩？")
                .build();
        QwenParam param = QwenParam
                .builder()
                .model(Generation.Models.QWEN_PLUS)
                .resultFormat(QwenParam.ResultFormat.MESSAGE)
                .messages(Arrays.asList(userMsg))
                .topP(0.8)
                .incrementalOutput(true) // get streaming output incrementally
                .build();
        Semaphore semaphore = new Semaphore(0);
        StringBuilder fullContent = new StringBuilder();
        gen.streamCall(param, new ResultCallback<GenerationResult>() {

            @Override
            public void onEvent(GenerationResult message) {
                fullContent.append(message.getOutput().getChoices().get(0).getMessage().getContent());
                System.out.println(message);
            }
            @Override
            public void onError(Exception err){
                System.out.println(String.format("Exception: %s", err.getMessage()));
                semaphore.release();
            }

            @Override
            public void onComplete(){
                System.out.println("Completed");
                semaphore.release();
            }

        });
        semaphore.acquire();
        System.out.println("Full content: \n" + fullContent.toString());
    }

}
