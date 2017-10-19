package com.leFinance.creditLoan.bizRule.service.utils;

import com.leFinance.creditLoan.bizRule.bo.RuleLoadBo;
import com.leFinance.creditLoan.bizRule.bo.RuleVersionBo;
import com.leFinance.creditLoan.bizRule.common.utils.FileUtil;
import com.leFinance.creditLoan.bizRule.common.utils.KieUtil;
import com.leFinance.creditLoan.bizRule.dao.RuleInfoMapper;
import com.leFinance.creditLoan.bizRule.domain.RuleInfo;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: zhulili1
 * @date: 2017/10/16
 * @description: 规则加载服务类
 */
@Slf4j
@Service
public class RuleLoadService {

    @Autowired
    private RuleInfoMapper ruleInfoMapper;

    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 设置KieContainerMap
     **/
    public void loadRule(String groupId, String artifactId, String version){
        // 日志前缀
        final String logPrefix = "设置kieContainerMap, ";

        try{
            RuleVersionBo ruleVersionBo = new RuleVersionBo(groupId, artifactId, version);
            // 查询 drl
            Resource[] resources = getKieResources(ruleVersionBo);
            // 查询 kmodule, containerName
            RuleLoadBo ruleLoadBo = getRuleInfo(ruleVersionBo);
            // 绑定 containerName, KieContainer
            KieUtil.loadRule(groupId, artifactId, version, ruleLoadBo.getKmodule(), resources);
        } catch (Exception e) {
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
        }
    }

    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 生成Resource
     * @param ruleVersion
     * @return
     */
    public Resource[] getKieResources(RuleVersionBo ruleVersion){
        // 日志前缀
        final String logPrefix = "查询drl信息, ";

        log.info("{}传入参数, {}", logPrefix, ruleVersion);
        try{
            List<RuleInfo> ruleInfoList = ruleInfoMapper.listSelectResourceByRuleVersion(ruleVersion);
            log.info("{}返回列表, {}", logPrefix, ruleInfoList.toString());
            Resource[] resources = new Resource[ruleInfoList.size()];
            for (int i = 0; i < ruleInfoList.size(); ++i) {
                Resource resource = ResourceFactory.newFileResource(ruleInfoList.get(i).getDrlPath());
                resource.setTargetPath(ruleInfoList.get(i).getDrlTargetPath());
                resources[i] = resource;
            }
            log.info("{}生成Resource列表, {}", logPrefix, resources);
            return resources;
        } catch(Exception e){
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常"+e.getMessage());
        }
    }
    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 查询KieModule, ContainerName信息
     **/
    public RuleLoadBo getRuleInfo(RuleVersionBo ruleVersion){
        // 日志前缀
        final String logPrefix = "查询KieModule, ContainerName信息, ";

        log.info("{}传入参数, {}", logPrefix, ruleVersion);
        try{
            RuleInfo ruleInfo = ruleInfoMapper.selectRuleInfoByRuleVersion(ruleVersion);
            log.info("{}查询RuleInfo结果{}", logPrefix, ruleInfo.toString());
            RuleLoadBo ruleLoadBo = new RuleLoadBo();
            ruleLoadBo.setContainerName(ruleInfo.getContainerName());
            ruleLoadBo.setKmodule(FileUtil.readFileToString(ruleInfo.getKmodulePath()));
            log.info("{}生成{}", logPrefix, ruleLoadBo.toString());
            return ruleLoadBo;
        } catch(Exception e){
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix+"异常"+e.getMessage());
        }
    }


}
