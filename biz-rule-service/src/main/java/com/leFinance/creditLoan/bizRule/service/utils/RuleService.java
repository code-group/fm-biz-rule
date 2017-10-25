package com.leFinance.creditLoan.bizRule.service.utils;

import com.google.common.base.Function;
import com.leFinance.creditLoan.bizRule.bo.RuleCreateBo;
import com.leFinance.creditLoan.bizRule.bo.RuleVersionBo;
import com.leFinance.creditLoan.bizRule.common.utils.KieUtil;
import com.leFinance.creditLoan.bizRule.dao.RuleInfoMapper;
import com.leFinance.creditLoan.bizRule.domain.RuleInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import org.springframework.transaction.annotation.Transactional;

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
     * @Description: 重新加载规则
     * @param ruleVersion
     */
    public void reloadRule(RuleVersionBo ruleVersion){
        // 日志前缀
        final String logPrefix = "重新加载规则, ";
        try{
            log.info("{}传入参数{}", logPrefix, ruleVersion);
            // 查询 drl
            String[] drls = ruleLoadService.getDrlArray(ruleVersion);
            // 重新加载规则
            KieUtil.loadRule(ruleVersion.getGroupId(), ruleVersion.getArtifactId(), ruleVersion.getVersion(), drls);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
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
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
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
    /**
     * created by zhulili1, on 2017/10/18
     * @Description: 创建规则
     * @param ruleCreateBo
     */
    public void createRuleInfo(RuleCreateBo ruleCreateBo){
        // 日志前缀
        final String logPrefix = "创建规则, ";

        try{
            log.info("{}传入参数, {}", logPrefix, ruleCreateBo.toString());
            RuleInfo ruleInfo = new RuleInfo();
            BeanUtils.copyProperties(ruleCreateBo, ruleInfo);
            ruleInfo.setCreateTime(new Date());
            log.info("{}插入规则记录, {}", logPrefix, ruleInfo);
            ruleInfoMapper.insertSelective(ruleInfo);
        } catch(Exception e){
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * created by zhulili1, on 2017/10/23
     * @Description: 查询规则
     * @param groupId
     * @param artifactId
     * @return
     */
    public List<RuleVersionBo> queryRuleVersionList(String groupId, String artifactId){
        // 日志前缀
        final String logPrefix = "查询规则, ";

        try{
            log.info("{}传入参数, {}, {}", logPrefix, groupId, artifactId);
            RuleVersionBo ruleCreateBo = new RuleVersionBo();
            ruleCreateBo.setGroupId(groupId);
            ruleCreateBo.setArtifactId(artifactId);
            List<RuleInfo> ruleInfoList = ruleInfoMapper.listSelectRuleVersion(ruleCreateBo);
            List<RuleVersionBo> ruleVersionBoList = Lists.transform(ruleInfoList, new Function<RuleInfo, RuleVersionBo>() {
                @Override
                public RuleVersionBo apply(RuleInfo ruleInfo){
                    RuleVersionBo ruleVersionBo = new RuleVersionBo();
                    ruleVersionBo.setGroupId(ruleInfo.getGroupId());
                    ruleVersionBo.setArtifactId(ruleInfo.getArtifactId());
                    ruleVersionBo.setVersion(ruleInfo.getVersion());
                    return ruleVersionBo;
                }
            });
            return ruleVersionBoList;
        } catch(Exception e) {
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
        }
        return null;
    }

    /**
     * created by zhulili1, on 2017/10/23
     * @Description: 查询规则信息列表
     **/
    public List<RuleInfo> queryRuleInfoList(String groupId, String artifactId, String version) {
        // 日志前缀
        final String logPrefix = "查询规则信息列表, ";

        try{
            log.info("{}传入参数, {}, {}, {}", logPrefix, groupId, artifactId, version);
            RuleVersionBo ruleVersionBo = new RuleVersionBo(groupId, artifactId, version);
            List<RuleInfo> ruleInfoList = ruleInfoMapper.listSelectRuleInfoByRuleVersion(ruleVersionBo);
            for (RuleInfo ruleInfo : ruleInfoList) {
                // 仅返回文件内容的前30个字符
                if (ruleInfo.getDrl().length() > 30){
                    ruleInfo.setDrl(ruleInfo.getDrl().substring(0, 30) + "...");
                }
            }
            log.info("{}结果: {}", logPrefix, ruleInfoList.toString());
            return ruleInfoList;
        } catch(Exception e) {
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
        }
        return null;
    }

    /**
     * created by zhulili1, on 2017/10/23
     * @Description: 查询规则信息
     **/
    public RuleInfo queryRuleInfo(String pid) {
        // 日志前缀
        final String logPrefix = "查询规则信息, ";

        try{
            log.info("{}传入参数, pid = {}", logPrefix, pid);
            RuleInfo ruleInfo = ruleInfoMapper.selectByPrimaryKey(Integer.parseInt(pid));
            log.info("{}结果: {}", logPrefix, ruleInfo.toString());
            return ruleInfo;
        } catch(Exception e) {
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
        }
        return null;
    }

    /**
     * created by zhulili1, on 2017/10/23
     * @Description: 更新规则信息
     **/
    public void updateDrl(RuleInfo ruleInfo) {
        // 日志前缀
        final String logPrefix = "更新规则信息, ";

        try{
            log.info("{}传入参数, {}", logPrefix, ruleInfo);
            ruleInfo.setUpdateTime(new Date());
            ruleInfoMapper.updateByPrimaryKeySelective(ruleInfo);
            log.info("{}结果: {}", logPrefix, ruleInfo.toString());
        } catch(Exception e) {
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
        }
    }
    /**
     * created by zhulili1, on 2017/10/23
     * @Description: 删除一组规则信息
     **/
    public void delRuleByVersion(RuleVersionBo ruleVersionBo) {
        // 日志前缀
        final String logPrefix = "删除一组规则信息, ";

        try{
            log.info("{}传入参数, {}", logPrefix, ruleVersionBo.toString());
            ruleInfoMapper.delRuleByVersion(ruleVersionBo);
        } catch(Exception e) {
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常" + e.getMessage());
        }
    }
    /**
     * created by zhulili1, on 2017/10/23
     * @Description: 删除一条规则文件
     **/
    public void delDrl(String pid) {
        // 日志前缀
        final String logPrefix = "删除一条规则文件, ";

        try{
            log.info("{}传入参数, pid = {}", logPrefix, pid);
            ruleInfoMapper.deleteByPrimaryKey(Integer.parseInt(pid));
        } catch(Exception e) {
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常" + e.getMessage());
        }
    }
    /**
     * created by zhulili1, on 2017/10/25
     * @Description: 创建并加载规则
     **/
    @Transactional(rollbackFor=Exception.class)
    public void createAndLoadRule(RuleCreateBo ruleCreateBo){
        createRuleInfo(ruleCreateBo);
        ruleLoadService.loadRule(ruleCreateBo.getGroupId(), ruleCreateBo.getArtifactId(), ruleCreateBo.getVersion());
    }

    /**
     * created by zhulili1, on 2017/10/25
     * @Description: 修改drl并加载规则
     **/
    @Transactional(rollbackFor=Exception.class)
    public void updateDrlAndLoadRule(RuleInfo ruleInfo){
        updateDrl(ruleInfo);
        ruleLoadService.loadRule(ruleInfo.getGroupId(), ruleInfo.getArtifactId(), ruleInfo.getVersion());
    }
}
