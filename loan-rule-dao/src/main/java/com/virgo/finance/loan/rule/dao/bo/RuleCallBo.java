package com.virgo.finance.loan.rule.dao.bo;

import lombok.Data;

/**
 * @author: zhulili1
 * date: 2017/10/19
 * description: 调用规则参数
 */

@Data
public class RuleCallBo {

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
     * container名称
     * 表字段： RULE_INFO.CONTAINER_NAME
     * </pre>
     *
     */
    private String containerName;


    /**
     * <pre>
     * ksession名称
     * 表字段： RULE_INFO.KSESSION_NAME
     * </pre>
     *
     */
    private String ksessionName;
}
