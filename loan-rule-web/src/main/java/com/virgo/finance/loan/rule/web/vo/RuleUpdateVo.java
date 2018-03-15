package com.virgo.finance.loan.rule.web.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RuleUpdateVo {

    /**
     * <pre>
     * 物理主键
     * 表字段： RULE_INFO.PID
     * </pre>
     *
     */
    private Integer pid;

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
     * 启用-1, 禁用-0
     * 表字段： RULE_INFO.STATUS
     * </pre>
     *
     */
    private Integer status;

    /**
     * <pre>
     * 规则文件描述
     * 表字段： RULE_INFO.DESCRIPTION
     * </pre>
     *
     */
    private String description;

    /**
     * <pre>
     * 创建时间
     * 表字段： RULE_INFO.CREATE_TIME
     * </pre>
     *
     */
    private Date createTime;

    /**
     * radio名称
     */
    private String radioName;
}
