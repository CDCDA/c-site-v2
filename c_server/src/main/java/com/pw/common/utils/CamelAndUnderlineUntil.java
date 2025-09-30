package com.pw.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author cyd
 * @date 2023/5/26 17:08
 * @description <字符串转为驼峰格式>
 **/
public class CamelAndUnderlineUntil {
    public static String camelUnder(String line){
        if(line==null||"".equals(line)){
            return "";
        }
        line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb=new StringBuffer();
        Pattern pattern= Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end()==line.length()?"":"_");
        }
        return sb.toString();
    }
}
