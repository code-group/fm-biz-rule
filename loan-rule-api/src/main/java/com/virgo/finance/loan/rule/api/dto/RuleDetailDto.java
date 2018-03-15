package com.virgo.finance.loan.rule.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhull
 * date: 2017/11/20
 * description: 规则详细信息
 * 属性与com.virgo.finance.loan.rule.dao.domain.RuleInfo对应
 */
@Data
public class RuleDetailDto implements Serializable{

    private static final long serialVersionUID = 1L;

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
     * drl规则内容
     * 表字段： RULE_INFO.DRL
     * </pre>
     *
     */
    private String drl;

    /**
     * <pre>
     * 规则文件描述
     * 表字段： RULE_INFO.DESCRIPTION
     * </pre>
     *
     */
    private String description;

}
