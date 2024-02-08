package com.example.product.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    public static final int FIRST = 1;
    public static final int SECOND = 2;
    public static final int THIRD = 3;

    @Id
    @CreatedBy
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;
    private Integer level;
    private Integer status; // 0未启用 1使用中
    @ReadOnlyProperty
    private LocalDateTime insertTime;
    @ReadOnlyProperty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime updateTime;
}
