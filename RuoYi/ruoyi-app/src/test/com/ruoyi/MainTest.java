package com.ruoyi;

import com.alibaba.druid.support.json.JSONUtils;
import com.ruoyi.app.util.JsonUtil;
import com.ruoyi.app.util.communication.Result;
import com.ruoyi.app.util.communication.ResultCode;

public class MainTest {

    public static void main(String[] args) {

        String json = JsonUtil.toString(
                Result.builder()
                        .code(ResultCode.unauthorized.getCode())
                        .msg("用户名不存在!")
                        .build());

        System.out.println(json);
    }
}
