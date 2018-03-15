package com.virgo.finance.loan.rule.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zhull
 * date: 2017/11/17
 * description: 查询规则信息页面封装参数
 */
@Data
public class RuleInfoQueryResPageDto implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 查询到的总条数
     */
    private Integer totalCount;

    /**
     * 查询到的规则信息列表
     */
    private List<RuleInfoQueryResDto> ruleInfoQueryResDtoList;
}
