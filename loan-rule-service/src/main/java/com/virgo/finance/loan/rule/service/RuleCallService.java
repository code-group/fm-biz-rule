package com.virgo.finance.loan.rule.service;

import com.virgo.finance.loan.rule.common.enums.RuleEnums;
import com.virgo.finance.loan.rule.common.utils.KieUtil;
import com.virgo.finance.loan.rule.dao.bo.RuleVersionBo;
import com.virgo.finance.loan.rule.service.utils.RuleLoadService;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhulili1
 * date: 2017/10/10
 * description: 调用规则服务
 */
@Slf4j
@Service
public class RuleCallService {

    @Autowired
    private RuleLoadService ruleLoadService;

    /**
     * created by zhulili1, on 2017/10/19
     * description: 调用规则
     * @update on 2017/10/26
     * @param ruleVersionBo
     * @param dataMap
     * @return
     */
    public int callRule(RuleVersionBo ruleVersionBo, Map<String, Object> dataMap) {
        // 日志前缀
        final String logPrefix = "调用规则, ";
        try{
            log.info("{}传入参数, {}, {}", logPrefix, ruleVersionBo.toString(), dataMap.toString());
            // 查询启用的规则内容
            String[] drls = ruleLoadService.getDrlArray(ruleVersionBo);
            if (drls == null || drls.length == 0) {
                // 未查询到启用的规则
                return RuleEnums.RuleCode.OMITTED_RULE.getCode();
            }
            // 设置版本号为启用版本
            ruleVersionBo.setVersion(RuleEnums.Version.WORKING_VERSION.code);
            KieSession kieSession = KieUtil.getKieSession(ruleVersionBo.getGroupId(), ruleVersionBo.getArtifactId(),
                    ruleVersionBo.getVersion());
            if(kieSession == null) {
                // 规则未加载, 进行加载
                log.info("{}规则未加载, 加载规则{}", logPrefix, ruleVersionBo);
                ruleLoadService.loadRule(ruleVersionBo, drls);
                kieSession = KieUtil.getKieSession(ruleVersionBo.getGroupId(), ruleVersionBo.getArtifactId(),
                        ruleVersionBo.getVersion());
            }
            dataMap = dataMap == null ? new HashMap<>() : dataMap;
            // 传入数据, 触发规则
            kieSession.insert(dataMap);
            kieSession.fireAllRules();
            log.info("{}返回数据, {}", logPrefix, dataMap);
            return RuleEnums.RuleCode.DEAL_SUCCESS.getCode();
        } catch(Exception e){
            // 调用规则失败
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            return RuleEnums.RuleCode.DEAL_FAILED.getCode();
        }
    }

    /**
     * created by zhulili1, on 2017/10/31
     * description: 根据版本调用规则
     **/
    public Map<String, Object> testCallRule(RuleVersionBo ruleVersionBo, Map<String, Object> dataMap) {
        // 日志前缀
        final String logPrefix = "根据版本调用规则, ";
        try {
            log.info("{}入参, {}", logPrefix, ruleVersionBo.toString());
            // 加载规则
            ruleLoadService.loadRule(ruleVersionBo);
            KieSession kieSession = KieUtil.getKieSession(ruleVersionBo.getGroupId(), ruleVersionBo.getArtifactId(),
                    ruleVersionBo.getVersion());
            dataMap = dataMap == null ? new HashMap<>() : dataMap;
            // 调用规则
            kieSession.insert(dataMap);
            kieSession.fireAllRules();
            log.info("{}返参, {}", logPrefix, dataMap.toString());
            return dataMap;
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常: " + e.getMessage());
        }
    }
}
