package com.leFinance.creditLoan.bizRule.service.utils;

import com.leFinance.creditLoan.bizRule.bo.RuleVersionBo;
import com.leFinance.creditLoan.bizRule.common.utils.FileUtil;
import com.leFinance.creditLoan.bizRule.common.utils.KieUtil;
import com.leFinance.creditLoan.bizRule.dao.RuleInfoMapper;
import com.leFinance.creditLoan.bizRule.domain.RuleInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @Description: 加载规则
     **/
    public void loadRule(String groupId, String artifactId, String version){
        // 日志前缀
        final String logPrefix = "加载规则, ";

        try{
            RuleVersionBo ruleVersionBo = new RuleVersionBo(groupId, artifactId, version);
            // 查询 drl
            String[] drls = getDrlArray(ruleVersionBo);
            // 加载规则
            KieUtil.loadRule(groupId, artifactId, version, drls);
        } catch (Exception e) {
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 查询Drl
     * @param ruleVersion
     * @return
     */
    public String[] getDrlArray(RuleVersionBo ruleVersion){
        // 日志前缀
        final String logPrefix = "查询drl信息, ";

        log.info("{}传入参数, {}", logPrefix, ruleVersion);
        try{
            List<String> drlList = ruleInfoMapper.listSelectDrlByRuleVersion(ruleVersion);
            log.info("{}返回列表, {}", logPrefix, drlList.toString());
            String[] drls = drlList.toArray(new String[drlList.size()]);
            return drls;
        } catch(Exception e){
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常"+e.getMessage());
        }
    }

}
