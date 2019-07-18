package com.ruoyi.app.util.communication;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResultCode {

    /**
     * 请求成功
     */
    SUCCESS(200,"请求成功!"),

    unauthorized(401,"未登录"),

    FORBIDDEN(403,"未授权"),
    /**
     * 参数错误
     */
    PARAM_ERROR(405,"参数错误"),
    /**
     * 系统异常
     */
    SYSTEM_ERROR(500,"系统异常");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
