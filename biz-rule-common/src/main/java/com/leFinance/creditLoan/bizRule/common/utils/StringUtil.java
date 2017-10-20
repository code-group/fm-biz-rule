package com.leFinance.creditLoan.bizRule.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author: zhulili1
 * @date: 2017/10/20
 * @description: 字符串工具类
 */
@Slf4j
public class StringUtil {

    /**
     * created by zhulili1, on 2017/10/20
     * @Description: 字符串转码
     **/
    public static String transformCode(String str) {
        try{
            if(str != null) {
                str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
            }
        } catch(Exception e){
            log.error("字符串{}转码异常：{}", str, e.getMessage(), e);
        }

        return str;
    }
    /**
     * created by zhulili1, on 2017/10/20
     * @Description: 对象中的字符串转码
     **/
    public static void transformCode(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field: fields) {
            if(field.getType().getName().equals("java.lang.String")) {
                field.setAccessible(true);
                try {
                    field.set(object, transformCode((String)field.get(object)));
                } catch (IllegalAccessException e) {
                    log.error("转码异常: {}", e.getMessage());
                }
            }
        }
    }


}
