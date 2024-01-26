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
public class Spu {
    @Id
    @CreatedBy
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String spuName;
    private String subTitle;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cid1;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cid2;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cid3;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long brandId;
    // 是否上架 0否 1是
    private Integer saleStatus;
    @ReadOnlyProperty
    private LocalDateTime insertTime;
    @ReadOnlyProperty
    private LocalDateTime updateTime;
}
