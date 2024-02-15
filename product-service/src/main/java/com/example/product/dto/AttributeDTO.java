package com.example.product.dto;

import com.example.product.po.Attribute;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttributeDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    public String categoryName;
    // 是否为数字类型 0否 1是
    private Integer isNumeric;
    private String unit;
    // 是否为sku通用属性 0否 1是
    private Integer isGeneric;
    private String value;
}
