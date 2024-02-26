package com.example.user.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.common.vo.RequestAttributeConstant;
import com.example.common.vo.ResultVO;
import com.example.user.po.Image;
import com.example.user.service.ImageService;
import com.example.user.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/image")
@Slf4j
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final JwtUtils jwtUtils;

    @PostMapping("/images")
    public Mono<ResultVO> postImage(@RequestBody Image image) {
        return imageService.addImage(image)
                .then(Mono.just(ResultVO.success(Map.of())));
    }

    @GetMapping("/images/{page}/{pageSize}")
    public Mono<ResultVO> getImages(@PathVariable int page, @PathVariable int pageSize, ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(RequestAttributeConstant.TOKEN);
        DecodedJWT decode = jwtUtils.decode(token);
        Long uid = decode.getClaim(RequestAttributeConstant.UID).asLong();
        return imageService.getImagesCount(uid)
                .flatMap(total -> imageService.listImages(uid, page, pageSize)
                        .map(images -> ResultVO.success(Map.of("images", images, "total", total))));
    }

    @DeleteMapping("/images/{id}")
    public Mono<ResultVO> deleteImages(@PathVariable long id) {
        return imageService.deleteImage(id).then(Mono.just(ResultVO.success(Map.of())));
    }
}
