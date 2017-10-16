package com.leFinance.creditLoan.bizRule.service;

import com.leFinance.creditLoan.bizRule.common.utils.BeanUtil;
import com.leFinance.creditLoan.bizRule.common.utils.KieContainerUtil;
import com.leFinance.creditLoan.bizRule.fact.ContractFact;
import lecreditcontract.dto.CreateContractReqDto;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
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
        final String logPrefix = "判断是否创建融资担保委托合同,";
        try{
            ContractFact contractFact = (ContractFact)BeanUtil.setBeanAttributes(ContractFact.class, dataMap);
            log.info("{}创建{}", logPrefix, contractFact);
            KieSession contractKieSession = KieContainerUtil.getKieSession(contractContainer, contractKsession);
            contractKieSession.insert(contractFact);
            contractKieSession.fireAllRules();
            return contractFact.getCreateDBContract();
        } catch(Exception e){
            log.error("{}异常{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix+"异常:"+e.getMessage());
        }

    }
}
