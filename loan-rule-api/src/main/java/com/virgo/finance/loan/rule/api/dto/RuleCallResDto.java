package com.virgo.finance.loan.rule.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: zhull
 * date: 2017/11/22
 * description: 测试规则, 调用规则返回结果
 */
@Data
public class RuleCallResDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 生成请求参数
     */
    Map<String, Object> dataReq;

    /**
     * 返回结果
     */
    Map<String, Object> result;

}
