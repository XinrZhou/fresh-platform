package com.example.aigcservice.service;

import com.tencentcloudapi.aiart.v20221229.AiartClient;
import com.tencentcloudapi.aiart.v20221229.models.TextToImageRequest;
import com.tencentcloudapi.aiart.v20221229.models.TextToImageResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImageGenerationService {
    @Value("${my.SecretId}")
    private String secretId;
    @Value("${my.SecretKey}")
    private String secretKey;
    public TextToImageResponse addAiPainting(TextToImageRequest textToImageRequest) throws TencentCloudSDKException {
        Credential cred = new Credential(secretId, secretKey);
        AiartClient client = new AiartClient(cred, "ap-shanghai");
        TextToImageRequest req = new TextToImageRequest(textToImageRequest);
        TextToImageResponse resp = client.TextToImage(req);
        System.out.println(resp);
        return resp;
    };

}
