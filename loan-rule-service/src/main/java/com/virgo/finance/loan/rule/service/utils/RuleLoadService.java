package com.virgo.finance.loan.rule.service.utils;

import com.virgo.finance.loan.rule.common.utils.KieUtil;
import com.virgo.finance.loan.rule.dao.bo.RuleVersionBo;
import com.virgo.finance.loan.rule.dao.mapper.RuleInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: zhulili1
 * date: 2017/10/16
 * description: 规则加载服务类
 */
@Slf4j
@Service
public class RuleLoadService {

    @Autowired
    private RuleInfoMapper ruleInfoMapper;

    /**
     * created by zhulili1, on 2017/10/16
     * description: 加载启用规则
     * @param groupId
     * @param artifactId
     * @param version
     * @return
     */
    public boolean loadRule(String groupId, String artifactId, String version){
        // 日志前缀
        final String logPrefix = "加载规则, ";

        try{
            log.info("{}groupId = {}, artifactId = {}, version = {}", logPrefix, groupId, artifactId, version);
            RuleVersionBo ruleVersionBo = new RuleVersionBo(groupId, artifactId, version);
            // 查询 drl
            String[] drls = getDrlArray(ruleVersionBo);
            if (drls == null || drls.length == 0) {
                // 未查询到启用的规则
                return false;
            }
            // 加载规则
            KieUtil.loadRule(groupId, artifactId, version, drls);
            return true;
        } catch (Exception e) {
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * created by zhulili1, on 2017/10/16
     * @update on 2017/10/26
     * description: 查询Drl（启用的规则）
     * @param ruleVersion
     * @return
     */
    public String[] getDrlArray(RuleVersionBo ruleVersion){
        // 日志前缀
        final String logPrefix = "查询drl信息, ";

        log.info("{}传入参数, {}", logPrefix, ruleVersion);
        try{
            // 查询启用的规则
            List<String> drlList = ruleInfoMapper.listSelectWorkingDrlByVersion(ruleVersion);
            log.info("{}返回列表, {}", logPrefix, drlList.toString());
            String[] drls = drlList.toArray(new String[drlList.size()]);
            return drls;
        } catch(Exception e){
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常"+e.getMessage());
        }
    }
    /**
     * created by zhulili1, on 2017/10/27
     * description: 加载规则
     **/
    public void loadRule(RuleVersionBo ruleVersionBo, String[] drls) {
        // 日志前缀
        final String logPrefix = "加载规则, ";
        try {
            log.info("{}入参: {}, drls = {}", logPrefix, ruleVersionBo.toString(), drls.toString());
            KieUtil.loadRule(ruleVersionBo.getGroupId(), ruleVersionBo.getArtifactId(), ruleVersionBo.getVersion(), drls);
        } catch(Exception e){
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常" + e.getMessage());
        }
    }

    /**
     * created by zhulili1, on 2017/10/31
     * description: 根据版本加载规则
     **/
    public void loadRule(RuleVersionBo ruleVersionBo) {
        // 日志前缀
        final String logPrefix = "根据版本加载规则, ";
        try {
            log.info("{}入参, {}", logPrefix, ruleVersionBo.toString());
            List<String> drlList = ruleInfoMapper.listSelectDrlByRuleVersion(ruleVersionBo);
            String[] drls = drlList.toArray(new String[drlList.size()]);
            KieUtil.loadRule(ruleVersionBo.getGroupId(), ruleVersionBo.getArtifactId(), ruleVersionBo.getVersion(), drls);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常: " + e.getMessage());
        }
    }
}
