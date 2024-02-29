package com.example.oss.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class OssUtil {
    @Value("${oss.endPoint}")
    private String endPoint;
    @Value("${oss.accessKeyId}")
    String accessKeyId;
    @Value("${oss.accessKeySecret}")
    String accessKeySecret;
    @Value("${oss.bucketName}")
    String bucketName;

    public String uploadFile(InputStream inputStream, String fileName) {
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject(bucketName, fileName, inputStream);
            return "https://" + bucketName + "." + endPoint + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 关闭OSS客户端
            ossClient.shutdown();
        }
    }
}
