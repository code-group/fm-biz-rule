package com.leFinance.creditLoan.bizRule.interfaces.impl;

import com.leFinance.creditLoan.bizRule.interfaces.ContractBizRuleInterface;
import com.leFinance.creditLoan.bizRule.service.ContractBizRuleService;
import data.Message;
import data.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.leFinance.creditLoan.bizRule.common.enums.SystemExceptionCode.INTERFACE_CALL_SERVICE;

/**
 * @author: zhulili1
 * @date: 2017/10/11
 * @description: 合同业务规则
 */
@Slf4j
@Service
public class ContractBizRuleInterfaceImpl implements ContractBizRuleInterface {

    @Value("${contract.ksession.name}")
    private String contractSession;

    @Autowired
    private ContractBizRuleService contractBizRuleService;

    /**
     * created by zhulili1, on 2017/10/11
     *
     * @param dataMap
     * @Description: 判定是否创建融资担保委托合同
     *          返回，true: 创建，false：不创建
     */
    @Override
    public Message<Boolean> createDBContract(Map<String, Object> dataMap) {
        final String logPrefix = "判断是否创建融资担保委托合同,"; // 日志前缀
        try {
            boolean result = contractBizRuleService.createDBContract(dataMap);
            log.info("{}结果{}", logPrefix, result);
            return Messages.success(result);
        } catch (Exception e) {
            log.error("{}异常{}", logPrefix, e.getMessage(), e);
            return Messages.failed(INTERFACE_CALL_SERVICE.getCode(),INTERFACE_CALL_SERVICE.getMessage()+e.getMessage());
        }
    }
}
