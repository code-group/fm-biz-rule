package com.virgo.finance.loan.rule.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhull
 * date: 2017/11/22
 * description: 启用规则请求参数
 */
@Data
public class RuleLoadReqDto implements Serializable {
    private static final long serialVersionUID = 1L;

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

    public RuleLoadReqDto() {

    }

    public RuleLoadReqDto(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }
}
