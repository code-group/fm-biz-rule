package com.leFinance.creditLoan.bizRule.web.config.drools;

import com.leFinance.creditLoan.bizRule.service.utils.RuleLoadService;
import lombok.extern.slf4j.Slf4j;
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
        // 加载合同规则
//        ruleLoadService.loadRule(contractGroupId, contractArtifactId, contractVersion);
        // 加载测试规则
//        ruleLoadService.loadRule(testGroupId, testArtifactId, testVersion);
    }



}
