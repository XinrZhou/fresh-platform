package com.example.oss.service;

import com.example.oss.utils.OssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OssService {
    private final OssUtil ossUtil;

    public String uploadFile(FilePart filePart) throws IOException {
        Path tempFile = Files.createTempFile("head", filePart.filename());
        filePart.transferTo(tempFile.toFile()).subscribe();
        File file = tempFile.toFile();
        FileInputStream fileInputStream = new FileInputStream(file);
        String fileName = "headPic" + UUID.randomUUID() + ".png";
        String folder = "head";
        String url = ossUtil.uploadFile(fileInputStream, folder + "/" + fileName);
        if ("false".equals(url)) {
            return "Upload failed";
        } else {
            return url;
        }
    }
}
