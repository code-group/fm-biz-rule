package com.virgo.finance.loan.rule.dao.mapper;

import com.virgo.finance.loan.rule.dao.bo.RuleVersionBo;
import com.virgo.finance.loan.rule.dao.domain.RuleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RuleInfoMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(RuleInfo record);

    int insertSelective(RuleInfo record);

    RuleInfo selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(RuleInfo record);

    int updateByPrimaryKey(RuleInfo record);

    /**
     * created by zhulili1, on 2017/10/20
     * description: 查询drl列表
     **/
    List<String> listSelectDrlByRuleVersion(RuleVersionBo ruleVersionBo);

    /**
     * created by zhulili1, on 2017/10/18
     * description: 查询规则信息
     **/
    List<RuleInfo> listSelectRuleInfoByRuleVersion(RuleVersionBo ruleVersionBo);

    /**
     * created by zhulili1, on 2017/10/18
     * description: 批量插入规则信息
     **/
    void batchInsert(List<RuleInfo> ruleInfoList);


    /**
     * created by zhulili1, on 2017/10/18
     * description: 查询规则版本列表（模糊查询）
     **/
    List<RuleInfo> listSelectRuleVersion(RuleVersionBo ruleCreateBo);

    /**
     * created by zhulili1, on 2017/10/23
     * description: 删除一组规则信息
     **/
    void delRuleByVersion(RuleVersionBo ruleVersionBo);

    /**
     * created by zhulili1, on 2017/10/26
     * description: 通过groupId, artifactId, status = 1(启用状态)查询规则列表
     **/
    List<String> listSelectWorkingDrlByVersion(RuleVersionBo ruleVersionBo);

    /**
     * created by zhulili1, on 2017/10/26
     * description: 通过groupId, artifactId, version 改变规则状态
     **/
    void updateRuleStatusByVersion(@Param("groupId") String groupId, @Param("artifactId") String artifactId,
                                   @Param("version") String version);

    /**
     * created by zhulili1, on 2017/10/26
     * description: 通过groupId, artifactId, version 改变规则状态为禁用
     **/
    void updateStatusToUnload(RuleVersionBo ruleVersionBo);

    /**
     * created by zhulili1, on 2017/10/31
     * description: 通过groupId, artifactId 查询规则最大版本号
     **/
    String selectMaxVersion(RuleVersionBo ruleVersionBo);

    /**
     * created by zhulili1, on 2017/11/2
     * description: 通过groupId, artifactId 查询记录条数
     **/
    int countRecordByGroupArtifactId(@Param("groupId") String groupId, @Param("artifactId") String artifactId);

    /**
     * created by zhull, on 2017/11/16
     * description: 查询最大pid
     **/
    int getMaxPid();

    /**
     * created by zhull, on 2017/11/17
     * description: 通过groupId, artifactId 查询记录条数
     **/
    int countDistinctVersionByGroupArtifactId(@Param("groupId") String groupId, @Param("artifactId") String artifactId);

    /**
     * created by zhull, on 2017/11/17
     * description: 查询分页信息
     **/
    List<RuleInfo> selectRuleInfoPage(@Param("groupId") String groupId, @Param("artifactId") String artifactId,
                                      @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * created by zhull, on 2017/11/20
     * description: 根据ruleNo查询记录列表
     **/
    List<RuleInfo> getRuleDetailByRuleNo(@Param("ruleNo") String ruleNo);

    /**
     * created by zhull, on 2017/11/22
     * description: 根据ruleNo改变规则状态
     **/
    void updateRuleStatusByRuleNo(@Param("ruleNo") String ruleNo);
}