package com.leFinance.creditLoan.bizRule.vo;

import com.leFinance.creditLoan.bizRule.domain.RuleInfo;
import lombok.Data;

import java.util.List;

/**
 * @author: zhulili1
 * @date: 2017/10/23
 * @description: 查看某规则版本的规则信息列表
 */
@Data
public class RuleDetailQueryVo {
    /**
     * <pre>
     * 规则组件GroupId
     * 表字段： RULE_INFO.GROUP_ID
     * </pre>
     *
     */
    private String groupId;

    /**
     * <pre>
     * 规则组件ArtifactId
     * 表字段： RULE_INFO.ARTIFACT_ID
     * </pre>
     *
     */
    private String artifactId;

    /**
     * <pre>
     * 规则版本号
     * 表字段： RULE_INFO.VERSION
     * </pre>
     *
     */
    private String version;

    /**
     * 规则信息列表
     */
    private List<RuleInfo> ruleInfoList;
}
