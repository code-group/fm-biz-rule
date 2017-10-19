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
     * kmodule文件位置
     * 表字段： RULE_INFO.KMODULE_PATH
     * </pre>
     *
     */
    private String kmodulePath;

    /**
     * <pre>
     * container名称
     * 表字段： RULE_INFO.CONTAINER_NAME
     * </pre>
     *
     */
    private String containerName;

    /**
     * <pre>
     * kbase名称
     * 表字段： RULE_INFO.KBASE_NAME
     * </pre>
     *
     */
    private String kbaseName;

    /**
     * <pre>
     * ksession名称
     * 表字段： RULE_INFO.KSESSION_NAME
     * </pre>
     *
     */
    private String ksessionName;

    /**
     * <pre>
     * 规则文件位置
     * 表字段： RULE_INFO.DRL_PATH
     * </pre>
     *
     */
    private String drlPath;

    /**
     * <pre>
     * 规则文件加载的目标位置
     * 表字段： RULE_INFO.DRL_TARGET_PATH
     * </pre>
     *
     */
    private String drlTargetPath;

    /**
     * <pre>
     * 规则文件描述
     * 表字段： RULE_INFO.DESCRIPTION
     * </pre>
     *
     */
    private String description;
}
