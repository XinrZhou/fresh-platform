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
    public static final int PARENT = 1;
    public static final int CHILD = 0;

    @Id
    @CreatedBy
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String categoryName;
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;
    private Integer isParent;
    @ReadOnlyProperty
    private LocalDateTime insertTime;
    @ReadOnlyProperty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime updateTime;
}
