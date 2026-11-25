package com.campus.animal.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiResponse<T>(int code, String msg, T data) {
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ok("success", data);
    }

    public static <T> ApiResponse<T> ok() {
        return ok(null);
    }

    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    @JsonProperty("message")
    public String message() {
        return msg;
    }
}
