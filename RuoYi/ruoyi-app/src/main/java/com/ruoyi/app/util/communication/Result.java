package com.ruoyi.app.util.communication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result<T> {

    private int code;//状态码
    private String msg;//消息
    private T data;//数据集

}
