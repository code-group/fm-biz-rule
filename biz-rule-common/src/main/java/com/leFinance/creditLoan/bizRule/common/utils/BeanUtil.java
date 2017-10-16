package com.leFinance.creditLoan.bizRule.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author: zhulili1
 * @date: 2017/10/12
 * @description: Bean工具类
 */
@Slf4j
public class BeanUtil {

    /**
     * created by zhulili1, on 2017/10/12
     * @Description: 为Bean的属性赋值
     **/
    public static Object setBeanAttributes(Class beanClass, Map<String, Object> attributeMap) {
        try{
            // 创建类的实例
            Object bean = beanClass.newInstance();
            // 得到类中的所有属性集合并赋值
            Field[] fields = beanClass.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                Field field = fields[i];
                field.setAccessible(true); // 设置属性是可以访问的
                field.set(bean, attributeMap.get(field.getName()));
            }
            return bean;
        } catch(Exception e){
            log.error("设置属性异常{}", e.getMessage(), e);
        }

        return null;
    }
}
