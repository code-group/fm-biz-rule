package com.virgo.finance.loan.rule.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zhull
 * date: 2017/11/16
 * description: 创建规则请求参数
 */
@Data
public class RuleCreateReqDto implements Serializable{
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
     * 规则文件内容列表
     */
    private List<String> drlList;

    /**
     * 规则文件描述列表
     */
    private List<String> descriptionList;

    /**
     * 备注
     */
    private String note;

}
