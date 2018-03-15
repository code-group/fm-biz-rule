package com.virgo.finance.loan.rule.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhull
 * date: 2017/11/17
 * description: 查询规则信息Dto
 */
@Data
public class RuleInfoQueryReqDto implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 当前页面号
     */
    private Integer currentPageNo;

    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 规则组件GroupId
     */
    private String groupId;

    /**
     * 规则组件ArtifactId
     */
    private String artifactId;

}
