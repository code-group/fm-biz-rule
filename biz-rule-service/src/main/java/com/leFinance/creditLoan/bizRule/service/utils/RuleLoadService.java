package com.leFinance.creditLoan.bizRule.service.utils;

import com.leFinance.creditLoan.bizRule.bo.RuleLoadBo;
import com.leFinance.creditLoan.bizRule.common.utils.FileUtil;
import com.leFinance.creditLoan.bizRule.dao.RuleInfoMapper;
import com.leFinance.creditLoan.bizRule.domain.RuleInfo;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.io.Resource;
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
     * @Description: 生成Resource
     * @param groupId
     * @param artifactId
     * @param version
     * @return
     */
    public Resource[] getKieResources(String groupId, String artifactId, String version){
        // 日志前缀
        final String logPrefix = "查询drl信息, ";

        log.info("{}传入参数, groupId={}, artifactId={}, version={}", logPrefix, groupId, artifactId, version);
        try{
            List<RuleInfo> ruleInfoList = ruleInfoMapper.listSelectResource(groupId, artifactId, version);
            log.info("{}返回列表，{}", logPrefix, ruleInfoList.toString());
            Resource[] resources = new Resource[ruleInfoList.size()];
            for (int i = 0; i < ruleInfoList.size(); ++i) {
                Resource resource = ResourceFactory.newFileResource(ruleInfoList.get(i).getDrlPath());
                resource.setTargetPath(ruleInfoList.get(i).getDrlTargetPath());
                resources[i] = resource;
            }
            log.info("{}生成Resource列表，{}", logPrefix, resources);
            return resources;
        } catch(Exception e){
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix+"异常"+e.getMessage());
        }
    }
    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 查询KieModule, ContainerName信息
     **/
    public RuleLoadBo getRuleInfo(String groupId, String artifactId, String version){
        // 日志前缀
        final String logPrefix = "查询KieModule, ContainerName信息, ";

        log.info("{}传入参数, groupId={}, artifactId={}, version={}", logPrefix, groupId, artifactId, version);
        try{
            RuleInfo ruleInfo = ruleInfoMapper.selectRuleInfo(groupId, artifactId, version);
            log.info("{},查询RuleInfo结果{}", logPrefix, ruleInfo.toString());
            RuleLoadBo ruleLoadBo = new RuleLoadBo();
            ruleLoadBo.setContainerName(ruleInfo.getContainerName());
            ruleLoadBo.setKmodule(FileUtil.readFileToString(ruleInfo.getKmodulePath()));
            log.info("{},生成{}", logPrefix, ruleLoadBo.toString());
            return ruleLoadBo;
        } catch(Exception e){
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix+"异常"+e.getMessage());
        }
    }


}
