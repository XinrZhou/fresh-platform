package com.example.user.po;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class User {
    public static final int ADMIN = 10;
    public static final int BUSINESS = 5;
    public static final int CONSUMER = 0;
    @Id
    @CreatedBy
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String userName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private  String password;
    @JsonIgnore
    private int role;
    private String phone;
    // 是否VIP  1 是 0 否
    private Integer isVip;
    private BigDecimal balance;
    @ReadOnlyProperty
    private LocalDateTime insertTime;
    @ReadOnlyProperty
    private LocalDateTime updateTime;
}
