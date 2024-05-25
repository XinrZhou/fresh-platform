package com.example.orderservice.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    public static final int OBLIGATION = 0;
    public static final int TO_BE_SHIPPED = 1;
    public static final int TO_BE_RECEIVED = 2;
    public static final int TO_BE_EVALUATED = 3;
    @Id
    @CreatedBy
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    // json 收货地址
    private String addressSpec;
    // json 订单规格
    private String orderSpec;
    // 订单类型 0配送 1自提
    private int type;
    // 订单状态 0待付款 1待发货 2待收货 3待评价
    private int status;
    private BigDecimal price;
    // 备注
    private String remark;
    @ReadOnlyProperty
    private LocalDateTime insertTime;
    @ReadOnlyProperty
    private LocalDateTime updateTime;
}
