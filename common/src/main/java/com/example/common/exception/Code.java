package com.example.common.exception;

public enum Code {
    LOGIN_ERROR(400, "用户名密码错误"),
    BAD_REQUEST(400, "请求错误"),
    UNAUTHORIZED(401, "未登录"),
    TOKEN_EXPIRED(403, "过期请重新登录"),
    FORBIDDEN(403, "无权限"),
    REGISTER_ERROR(201, "该账号已注册");
    private final int code;
    private final String message;

    Code(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
