package com.leFinance.creditLoan.bizRule.service;

import com.leFinance.creditLoan.bizRule.bo.RuleCallBo;
import com.leFinance.creditLoan.bizRule.common.utils.KieUtil;
import com.leFinance.creditLoan.bizRule.service.utils.RuleLoadService;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: zhulili1
 * @date: 2017/10/10
 * @description: 调用规则服务
 */
@Slf4j
@Service
public class RuleCallService {

    @Autowired
    private RuleLoadService ruleLoadService;

    /**
     * created by zhulili1, on 2017/10/19
     * @Description: 调用规则
     **/
    public void callRule(RuleCallBo ruleCallBo, Map<String, Object> dataMap) {
        // 日志前缀
        final String logPrefix = "调用规则, ";
        try{
            log.info("{}传入参数, {}, {}", logPrefix, ruleCallBo.toString(), dataMap);
            KieSession kieSession = KieUtil.getKieSession(ruleCallBo.getGroupId(), ruleCallBo.getArtifactId(),
                    ruleCallBo.getVersion(), ruleCallBo.getKsessionName());
            if(kieSession == null) {  // 实时加载规则
                log.info("{}规则未加载，加载规则{}", logPrefix, ruleCallBo);
                ruleLoadService.loadRule(ruleCallBo.getGroupId(), ruleCallBo.getArtifactId(), ruleCallBo.getVersion());
                kieSession = KieUtil.getKieSession(ruleCallBo.getGroupId(), ruleCallBo.getArtifactId(),
                        ruleCallBo.getVersion(), ruleCallBo.getKsessionName());
                if(kieSession == null){
                    log.error("{}规则不存在: {}", logPrefix, ruleCallBo.toString());
                    throw new RuntimeException("规则不存在: " + ruleCallBo.toString());
                }
            }
            // 传入数据，触发规则
            kieSession.insert(dataMap);
            kieSession.fireAllRules();
            log.info("{}返回数据, {}", logPrefix, dataMap);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix+"异常:"+e.getMessage());
        }

    }
}
