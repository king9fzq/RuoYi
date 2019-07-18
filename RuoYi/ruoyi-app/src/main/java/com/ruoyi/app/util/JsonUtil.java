package com.ruoyi.app.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.exception.MyException;

/**
 * Created by 冯章棋 on 2016/9/27 0027.
 */
public class JsonUtil {

    private static final ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        //设置自动忽略不需要的字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    public static <T>  T toMyObject(String str,Class<T> t){

        try {

            return  objectMapper.readValue(str, t);

        }catch (Exception e){

            e.printStackTrace();

            throw new MyException("json转换为自定义对象出错!");
        }
    }

    public static String toString(Object object){

        try {

            return objectMapper.writeValueAsString(object);

        }catch (Exception e){
            e.printStackTrace();
            throw new MyException("对象转换成json格式出错!");
        }
    }
}
