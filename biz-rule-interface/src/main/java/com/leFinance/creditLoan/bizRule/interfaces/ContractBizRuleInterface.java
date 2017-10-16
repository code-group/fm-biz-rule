package com.leFinance.creditLoan.bizRule.interfaces;

import data.Message;

import java.util.Map;

/**
 * @author: zhulili1
 * @date: 2017/10/11
 * @description: 合同业务规则
 */
public interface ContractBizRuleInterface {

    /**
     * created by zhulili1, on 2017/10/11
     * @Description: 判定是否创建融资担保委托合同
     **/
    Message<Boolean> createDBContract(Map<String, Object> dataMap);
}
