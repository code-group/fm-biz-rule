package com.virgo.finance.loan.rule.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhull
 * date: 2017/11/17
 * description: 查询规则列表返回数据
 */
@Data
public class RuleInfoQueryResDto implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 规则编号
     */
    private String ruleNo;

    /**
     * 规则组件GroupId
     */
    private String groupId;

    /**
     * 规则组件ArtifactId
     */
    private String artifactId;

    /**
     * 规则版本号
     */
    private String version;

    /**
     * 启用-1, 禁用-0
     */
    private Integer status;

    /**
     * 备注
     */
    private String note;

}
