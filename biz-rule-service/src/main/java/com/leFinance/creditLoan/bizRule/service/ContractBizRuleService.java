package com.leFinance.creditLoan.bizRule.service;

import com.leFinance.creditLoan.bizRule.common.utils.KieUtil;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: zhulili1
 * @date: 2017/10/10
 * @description:
 */
@Slf4j
@Service
public class ContractBizRuleService {

    @Value("${contract.kie.container.name}")
    private String contractContainer;

    @Value("${contract.ksession.name}")
    private String contractKsession;

    public boolean createDBContract(Map<String, Object> dataMap) {
        // 日志前缀
        final String logPrefix = "判断是否创建融资担保委托合同, ";
        try{
            log.info("{}传入参数: {}", logPrefix, dataMap);
            KieSession contractKieSession = KieUtil.getKieSession(contractContainer, contractKsession);
            contractKieSession.insert(dataMap);
            contractKieSession.fireAllRules();
            return (boolean)dataMap.get("createDBContract");
        } catch(Exception e){
            log.error("{}异常{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix+"异常:"+e.getMessage());
        }

    }
}
