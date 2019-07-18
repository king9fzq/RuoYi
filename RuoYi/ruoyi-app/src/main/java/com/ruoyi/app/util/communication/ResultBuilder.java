package com.ruoyi.app.util.communication;

public class ResultBuilder {

    private ResultBuilder() {}

    public static <T> Result<T> success() {
        return of(ResultCode.SUCCESS, null);
    }

    public static <T> Result<T> success(T data) {

        return of(ResultCode.SUCCESS, data);
    }

    public static <T> Result<T> exception() {
        return of(ResultCode.SYSTEM_ERROR, null);
    }

    public static <T> Result<T> error(ResultCode code) {
        return of(code,null);
    }

    private static <T> Result<T> of(ResultCode code, T data) {
        return Result.<T>builder()
                .code(code.getCode())
                .msg(code.getMsg())
                .data(data)
                .build();
    }
}
