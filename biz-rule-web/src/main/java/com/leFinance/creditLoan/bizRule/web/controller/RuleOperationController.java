package com.leFinance.creditLoan.bizRule.web.controller;

import com.leFinance.creditLoan.bizRule.bo.RuleVersionBo;
import com.leFinance.creditLoan.bizRule.service.utils.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: zhulili1
 * @date: 2017/10/17
 * @description: 规则操作控制器
 */
@Slf4j
@RestController
@RequestMapping("rule")
public class RuleOperationController {

    @Autowired
    private RuleService reloadService;

    @RequestMapping("reload/{groupId}&{artifactId}&{version}.html")
    public String reloadRules(@PathVariable("groupId") String groupId, @PathVariable("artifactId") String artifactId,
                              @PathVariable("version") String version){
        // 日志前缀
        final String logPrefix = "重新加载规则, ";

        try{
            log.info("{}传入参数groupId={}, artifact={}, version={}", logPrefix, groupId, artifactId, version);
            RuleVersionBo ruleVersionBo = new RuleVersionBo(groupId, artifactId, version);
            reloadService.reloadRule(ruleVersionBo);
        } catch(Exception e){
            log.error("{}异常,{}", logPrefix, e.getMessage(), e);
            return "error: " + e.getMessage();
        }
        return "success";
    }
}
