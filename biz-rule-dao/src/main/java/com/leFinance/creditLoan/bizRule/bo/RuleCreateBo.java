package com.leFinance.creditLoan.bizRule.bo;

import lombok.Data;

/**
 * @author: zhulili1
 * @date: 2017/10/18
 * @description: 创建规则记录Bo
 */
@Data
public class RuleCreateBo {

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
     * <pre>
     * drl规则内容
     * 表字段： RULE_INFO.DRL
     * </pre>
     *
     */
    private String drl;

    /**
     * <pre>
     * 规则文件描述
     * 表字段： RULE_INFO.DESCRIPTION
     * </pre>
     *
     */
    private String description;
}
