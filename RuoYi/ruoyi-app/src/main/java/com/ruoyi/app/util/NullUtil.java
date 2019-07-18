package com.ruoyi.app.util;

public class NullUtil {

    public static boolean isNull(String str){

        if(null==str||str.length()==0) return true;
        return false;
    }

    public static boolean isNotNull(String str){
        return !isNull(str);
    }
}
