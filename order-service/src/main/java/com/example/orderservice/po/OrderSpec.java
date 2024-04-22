package com.example.orderservice.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSpec {
    private Long id;
    private Long userId;
    private Long skuId;
    private int count;
    private String skuName;
    private String imageUrl;
    private double originPrice;
    private double discountPrice;
    private String unit;
}
