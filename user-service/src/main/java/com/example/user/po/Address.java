package com.example.user.po;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @CreatedBy
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    private String province;
    private String city;
    private String town;
    private String street;
    private String phone;
    private String contact;
    // 是否默认 1 默认 0 否
    private Integer isDefault;
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;
}
