package com.example.orderservice.service;

import com.example.feignapi.client.ProductClient;
import com.example.orderservice.dto.CartDTO;
import com.example.orderservice.po.Cart;
import com.example.orderservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final ProductClient productClient;
    private final ConnectionFactory  connectionFactory;

    public Mono<Cart> addCart(Cart cart) {
        return cartRepository.findByUserIdAndTypeOrderByUpdateTime(cart.getUserId(), Cart.UNSETTLED).collectList()
                .flatMap(carts -> {
                    for (Cart c : carts) {
                        if (c.getSkuId().equals(cart.getSkuId())) {
                            c.setCount(c.getCount() + 1);
                            return cartRepository.save(c);
                        }
                    }
                    cart.setCount(1);
                    return cartRepository.save(cart);
                });
    }

    public Mono<Void> deleteByIds(List<Long> cartIds) {
        return cartRepository.deleteByIdIn(cartIds);
    }


    public Mono<List<CartDTO>> listCarts(long uid) {
        return cartRepository.findByUserIdAndTypeOrderByUpdateTime(uid, Cart.UNSETTLED).collectList()
                .flatMapMany(Flux::fromIterable)
                .flatMap(cart -> productClient.getSku(cart.getSkuId())
                        .map(sku -> CartDTO.builder()
                                .id(cart.getId())
                                .userId(uid)
                                .skuId(cart.getSkuId())
                                .count(cart.getCount())
                                .unit(sku.getUnit())
                                .originPrice(sku.getOriginPrice())
                                .discountPrice(sku.getDiscountPrice())
                                .imageUrl(sku.getImageUrl())
                                .skuName(sku.getName())
                                .build())
                ).collectList();

    }
}
