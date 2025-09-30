package com.pw.common.utils;

/***
 * @author cyd
 * @date 2023/6/9 17:58
 * @description <>
 **/
public class EmptyJugeUtil {
    public static boolean isEmpty(String str){
        if(str == null || str.equals("")){
            return true;
        }
        return false;
    }

    public static <T> boolean isEmpty(T entity){
        if(entity == null){
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Long entity){
        if(entity == null){
            return true;
        }
        return false;
    }
}
