package com.virgo.finance.loan.rule.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: zhulili1
 * date: 2017/10/19
 * description: 调用规则请求参数
 */

@Data
public class RuleReqDto implements Serializable {
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
     * 操作数据
     */
    private Map<String, Object> dataMap;
}
