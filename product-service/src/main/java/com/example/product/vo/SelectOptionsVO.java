package com.example.product.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectOptionsVO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long value;
    private String label;
}
