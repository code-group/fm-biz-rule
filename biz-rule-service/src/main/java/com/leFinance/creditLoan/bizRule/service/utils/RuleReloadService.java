package com.leFinance.creditLoan.bizRule.service.utils;

import com.leFinance.creditLoan.bizRule.bo.RuleLoadBo;
import com.leFinance.creditLoan.bizRule.common.utils.KieUtil;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: zhulili1
 * @date: 2017/10/16
 * @description: 重新加载规则服务类
 */
@Slf4j
@Service
public class RuleReloadService {

    @Autowired
    private RuleLoadService ruleLoadService;

    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 重新加载规则（新增container，或修改container）
     * @param groupId
     * @param artifactId
     * @param version
     */
    public void reloadRule(String groupId, String artifactId, String version){
        try{
            // 查询 drl
            Resource[] resources = ruleLoadService.getKieResources(groupId, artifactId, version);
            // 查询 kmodule, containerName
            RuleLoadBo ruleLoadBo = ruleLoadService.getRuleInfo(groupId, artifactId, version);
            // 重新创建 container
            KieContainer kieContainer = KieUtil.createAndGetKieContainer(groupId, artifactId, version,
                    ruleLoadBo.getKmodule(), resources);
            // 绑定 containerName, KieContainer
            KieUtil.kieContainerMap.put(ruleLoadBo.getContainerName(), kieContainer);
        } catch(Exception e){
            log.error("重新加载规则异常{}", e.getMessage(), e);
        }

    }
}
