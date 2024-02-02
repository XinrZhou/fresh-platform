package com.example.oss.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class OssUtil {
    String endPoint = "oss-cn-hangzhou.aliyuncs.com";
    String accessKeyId = "LTAI5tFMdUrkP8zpHh5NcGQB";
    String accessKeySecret = "FJjOBonpziQuTvJYdm4yeeROTx3Rxv";
    String bucketName = "fresh-platform-img";

    OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

    public String uploadFile(InputStream inputStream, String fileName) {
        try {
            ossClient.putObject(bucketName, fileName, inputStream);
        } catch (Exception e) {
            return "false";
        }
        return "https://"+bucketName+".oss-cn-hangzhou.aliyuncs.com/"+fileName;
    };
}
