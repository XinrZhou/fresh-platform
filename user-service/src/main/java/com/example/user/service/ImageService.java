package com.example.user.service;

import com.example.user.po.Image;
import com.example.user.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public Mono<Image> addImage(Image image) {
        return imageRepository.save(image);
    }

    public Mono<List<Image>> listImages(long uid, int page, int pageSize) {
        return imageRepository.findByUserId(uid, (page - 1) * pageSize, pageSize).collectList();
    }
    public Mono<Integer> getImagesCount(long uid) {
        return imageRepository.findCount(uid);
    }

    public Mono<Void> deleteImage(long id) {
        return imageRepository.deleteById(id);
    }
}
