package com.virgo.finance.loan.rule.dao.domain;


import lombok.Data;

import java.util.Date;

@Data
public class RuleCallRecord {
    /**
     * <pre>
     * 物理主键
     * 表字段： RULE_CALL_RECORD.PID
     * </pre>
     * 
     */
    private Integer pid;

    /**
     * <pre>
     * 请求参数
     * 表字段： RULE_CALL_RECORD.REQUEST_PARAMETER
     * </pre>
     * 
     */
    private String requestParameter;

    /**
     * <pre>
     * 响应结果
     * 表字段： RULE_CALL_RECORD.RESPONSE_RESULT
     * </pre>
     * 
     */
    private String responseResult;

    /**
     * <pre>
     * 创建时间
     * 表字段： RULE_CALL_RECORD.CREATION_TIME
     * </pre>
     * 
     */
    private Date creationTime;

}