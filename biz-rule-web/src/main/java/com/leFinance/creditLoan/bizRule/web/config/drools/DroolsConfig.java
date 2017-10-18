package com.leFinance.creditLoan.bizRule.web.config.drools;

import com.leFinance.creditLoan.bizRule.bo.RuleLoadBo;
import com.leFinance.creditLoan.bizRule.bo.RuleVersionBo;
import com.leFinance.creditLoan.bizRule.common.utils.KieUtil;
import com.leFinance.creditLoan.bizRule.common.utils.KieProjectUtil;
import com.leFinance.creditLoan.bizRule.service.utils.RuleLoadService;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: zhulili1
 * @date: 2017/10/12
 * @description: Kie工具类
 */
@Slf4j
@Component
public class DroolsConfig {

    @Autowired
    private RuleLoadService ruleLoadService;

    // contract config
    @Value("${contract.group.id}")
    private String contractGroupId;
    @Value("${contract.artifact.id}")
    private String contractArtifactId;
    @Value("${contract.version}")
    private String contractVersion;

    // test config
    @Value("${test.group.id}")
    private String testGroupId;
    @Value("${test.artifact.id}")
    private String testArtifactId;
    @Value("${test.version}")
    private String testVersion;

    @PostConstruct
    public void initDroolsConfig(){
        // 初始化kieContainerMap
        KieUtil.setKieContainerMap();

        // 加载合同规则
        updateKieContainerMap(contractGroupId, contractArtifactId, contractVersion);

        // 加载测试规则
        updateKieContainerMap(testGroupId, testArtifactId, testVersion);

    }

    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 设置KieContainerMap
     **/
    public void updateKieContainerMap(String groupId, String artifactId, String version){
        // 日志前缀
        final String logPrefix = "设置kieContainerMap, ";

        try{
            RuleVersionBo ruleVersionBo = new RuleVersionBo(groupId, artifactId, version);
            // 查询 drl
            Resource[] resources = ruleLoadService.getKieResources(ruleVersionBo);
            // 查询 kmodule, containerName
            RuleLoadBo ruleLoadBo = ruleLoadService.getRuleInfo(ruleVersionBo);
            // 绑定 containerName, KieContainer
            KieContainer kieContainer = KieUtil.createAndGetKieContainer(groupId, artifactId, version,
                    ruleLoadBo.getKmodule(), resources);
            KieUtil.kieContainerMap.put(ruleLoadBo.getContainerName(), kieContainer);
        } catch (Exception e) {
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
        }
    }

}
