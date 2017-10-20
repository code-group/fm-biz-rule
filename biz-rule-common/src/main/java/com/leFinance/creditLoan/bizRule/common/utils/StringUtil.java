package com.leFinance.creditLoan.bizRule.common.utils;

import lombok.extern.slf4j.Slf4j;

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
            str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch(Exception e){
            log.error("字符串{}转码异常：{}", str, e.getMessage(), e);
        }

        return str;
    }
}
