package com.virgo.finance.loan.rule.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: zhulili1
 * date: 2017/10/23
 * description: 查看某规则版本的规则信息列表
 */
@Data
public class RuleDetailResDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * <pre>
     * 规则编号=时间戳+下一个pid
     * 表字段： RULE_INFO.RULE_NO
     * </pre>
     *
     */
    private String ruleNo;

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
     * 备注
     * 表字段： RULE_INFO.NOTE
     * </pre>
     *
     */
    private String note;

    /**
     * <pre>
     * 创建时间
     * 表字段： RULE_INFO.CREATE_TIME
     * </pre>
     *
     */
    private Date createTime;

    /**
     * 规则信息列表
     */
    private List<RuleDetailDto> ruleInfoList;
}
