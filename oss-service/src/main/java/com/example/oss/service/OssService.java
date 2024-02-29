package com.example.oss.service;

import com.example.oss.utils.OssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

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

    public Mono<String> uploadFile(FilePart filePart) {
        final Path tempFile;
        try {
            tempFile = Files.createTempFile("head", filePart.filename());
        } catch (IOException e) {
            log.error("Error creating temporary file", e);
            return Mono.just("Upload failed");
        }

        return filePart
                .transferTo(tempFile)
                .then(Mono.fromSupplier(() -> {
                    try {
                        File file = tempFile.toFile();
                        String fileName = "headPic" + UUID.randomUUID() + ".jpg";
                        String folder = "head";
                        String url = ossUtil.uploadFile(new FileInputStream(file), folder + "/" + fileName);
                        if ("false".equals(url)) {
                            return "Upload failed";
                        } else {
                            return url;
                        }
                    } catch (IOException e) {
                        log.error("Error uploading file to OSS", e);
                        throw new RuntimeException(e);
                    } finally {
                        try {
                            Files.deleteIfExists(tempFile);
                        } catch (IOException e) {
                            log.error("Failed to delete temporary file: {}", tempFile, e);
                        }
                    }
                }))
                .onErrorResume(e -> {
                    log.error("Error transferring file to temporary location", e);
                    return Mono.just("Upload failed");
                });
    }
}
