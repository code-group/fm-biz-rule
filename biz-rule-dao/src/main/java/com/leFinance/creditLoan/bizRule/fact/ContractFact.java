package com.leFinance.creditLoan.bizRule.fact;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: zhulili1
 * @date: 2017/10/12
 * @description: 合同规则Fact类
 */
@Data
public class ContractFact {

    // 担保费
    private BigDecimal feeValue;

    // 是否创建担保委托合同
    private Boolean createDBContract;

}
