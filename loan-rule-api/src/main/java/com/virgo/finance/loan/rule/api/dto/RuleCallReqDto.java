package com.virgo.finance.loan.rule.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zhull
 * date: 2017/11/22
 * description: 测试规则, 调用规则请求参数
 */
@Data
public class RuleCallReqDto implements Serializable {
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

    /**
     * 请求参数类型列表
     */
    private List<String> typeList;

    /**
     * 请求参数key列表
     */
    private List<String> keyList;

    /**
     * 请求参数value列表
     */
    private List<String> valueList;
    
}
