package com.example.aigcservice.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resource {
    public static final int IMAGE = 0;
    public static final int CHAT = 1;
    @Id
    @CreatedBy
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    // 资源类型 0绘画 1写作
    private Integer type;
    // 状态 0禁用 1使用
    private Integer status;
    private String description;
    private BigDecimal price;
    private String unit;
    @ReadOnlyProperty
    private LocalDateTime insertTime;
    @ReadOnlyProperty
    private LocalDateTime updateTime;
}
