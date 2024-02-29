package com.example.aigcservice.po;

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
public class Resource {
    public static final int IMAGE = 0;
    public static final int CHAT = 1;
    @Id
    @CreatedBy
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    // AI服务类型 0绘画 1写作
    private Integer type;
    @ReadOnlyProperty
    private LocalDateTime insertTime;
    @ReadOnlyProperty
    private LocalDateTime updateTime;
}
