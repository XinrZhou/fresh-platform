package com.example.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpuDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    private String categoryName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long brandId;
    private String brandName;
    private String imageUrl;
    // 是否上架 0否 1是
    private Integer saleStatus;
    private String description;
}
