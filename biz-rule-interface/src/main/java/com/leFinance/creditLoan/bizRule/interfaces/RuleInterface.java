package com.leFinance.creditLoan.bizRule.interfaces;

import com.leFinance.creditLoan.bizRule.dto.RuleReqDto;
import data.Message;

import java.util.Map;

/**
 * @author: zhulili1
 * @date: 2017/10/11
 * @description: 规则接口
 */
public interface RuleInterface {

    /**
     * created by zhulili1, on 2017/10/11
     * @Description: 参数接收入口
     **/
    Message<Map<String, Object>> callRule(RuleReqDto ruleReqDto);
}
