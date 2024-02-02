package com.example.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class CategoryDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String categoryName;
    private String imageUrl;
    private Integer isParent;
    private String parentName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime updateTime;
}
