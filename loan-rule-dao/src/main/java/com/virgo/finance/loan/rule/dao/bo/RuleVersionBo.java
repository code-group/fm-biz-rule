package com.virgo.finance.loan.rule.dao.bo;

import lombok.Data;

/**
 * @author: zhulili1
 * date: 2017/10/18
 * description: 规则版本Bo
 */
@Data
public class RuleVersionBo {

    /**
     * 规则编号
     */
    private String ruleNo;

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
     * 备注
     */
    private String note;

    /**
     * <pre>
     * 启用-1, 禁用-0
     * 表字段： RULE_INFO.STATUS
     * </pre>
     *
     */
    private Integer status;

    public RuleVersionBo(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }
    public RuleVersionBo(String groupId, String artifactId, String version, String note) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.note = note;
    }
    public RuleVersionBo(){
    }
}
