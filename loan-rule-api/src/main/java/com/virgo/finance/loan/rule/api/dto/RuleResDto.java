package com.virgo.finance.loan.rule.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: zhulili1
 * date: 2017/10/26
 * description: 调用规则返回参数
 */
@Data
public class RuleResDto implements Serializable{

    /**
     * 处理结果编码 0-规则执行成功, 1-规则执行失败, 2-忽略的规则
     */
    private Integer code;

    /**
     * 处理结果消息
     * @see RuleResDto#code 的含义
     */
    private String message;

    /**
     * 执行规则返回的结果
     */
    private Map<String, Object> result;
}
