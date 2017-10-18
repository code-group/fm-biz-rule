package com.leFinance.creditLoan.bizRule.service.utils;

import com.leFinance.creditLoan.bizRule.bo.RuleLoadBo;
import com.leFinance.creditLoan.bizRule.bo.RuleVersionBo;
import com.leFinance.creditLoan.bizRule.common.utils.KieUtil;
import com.leFinance.creditLoan.bizRule.dao.RuleInfoMapper;
import com.leFinance.creditLoan.bizRule.domain.RuleInfo;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: zhulili1
 * @date: 2017/10/16
 * @description: 重新加载规则服务类
 */
@Slf4j
@Service
public class RuleService {

    @Autowired
    private RuleLoadService ruleLoadService;

    @Autowired
    private RuleInfoMapper ruleInfoMapper;

    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 重新加载规则（新增container，或修改container）
     * @param ruleVersion
     */
    public void reloadRule(RuleVersionBo ruleVersion){
        // 日志前缀
        final String logPrefix = "重新加载规则, ";
        try{
            log.info("{}传入参数{}", logPrefix, ruleVersion);
            // 查询 drl
            Resource[] resources = ruleLoadService.getKieResources(ruleVersion);
            // 查询 kmodule, containerName
            RuleLoadBo ruleLoadBo = ruleLoadService.getRuleInfo(ruleVersion);
            // 重新创建 container
            KieContainer kieContainer = KieUtil.createAndGetKieContainer(ruleVersion.getGroupId(), ruleVersion.getArtifactId(),
                    ruleVersion.getVersion(), ruleLoadBo.getKmodule(), resources);
            // 绑定 containerName, KieContainer
            KieUtil.kieContainerMap.put(ruleLoadBo.getContainerName(), kieContainer);
        } catch(Exception e){
            log.error("{}异常{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常" + e.getMessage());
        }

    }

    /**
     * created by zhulili1, on 2017/10/18
     * @Description: 为规则升级版本标识（仅升级版本号，原规则内容不变）
     * @param oldVersion
     * @param newVersion
     */
    public void upgradeRuleVersion(RuleVersionBo oldVersion, RuleVersionBo newVersion){
        // 日志前缀
        final String logPrefix = "为规则升级版本标识, ";
        try{
            log.info("{}传入参数, oldVersion={}, newVersion={}", logPrefix, oldVersion.toString(), newVersion.toString());
            List<RuleInfo> ruleInfoList = ruleInfoMapper.listSelectRuleInfoByRuleVersion(oldVersion);
            log.info("{}查询旧版本规则信息, 返回结果{}", logPrefix, ruleInfoList);
            if(ruleInfoList == null || ruleInfoList.size() == 0){
                throw new RuntimeException("无法升级版本号：查询旧版本规则信息, 返回结果为空");
            }
            updateVersion(ruleInfoList, newVersion);
            log.info("{}新版本RuleInfo为: {}", logPrefix, ruleInfoList.toString());
            ruleInfoMapper.batchInsert(ruleInfoList);
        } catch(Exception e){
            log.error("{}异常{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常" + e.getMessage());
        }
    }

    /**
     * created by zhulili1, on 2017/10/18
     * @Description: 修改ruleInfoList
     * @param ruleInfoList
     * @param newVersion
     */
    private void updateVersion(List<RuleInfo> ruleInfoList, RuleVersionBo newVersion) {
        for (RuleInfo ruleInfo : ruleInfoList) {
            ruleInfo.setGroupId(newVersion.getGroupId());
            ruleInfo.setArtifactId(newVersion.getArtifactId());
            ruleInfo.setVersion(newVersion.getVersion());
            ruleInfo.setCreateTime(new Date());
            ruleInfo.setUpdateTime(null);
        }
    }


}
