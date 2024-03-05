package com.example.common.constant;

public enum StatusEnum {
    UNUSED(0, "未使用"),
    IN_USE(1, "使用中");

    private int code;
    private String description;

    StatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
