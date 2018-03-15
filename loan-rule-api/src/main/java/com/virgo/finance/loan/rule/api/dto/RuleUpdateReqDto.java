package com.virgo.finance.loan.rule.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zhull
 * date: 2017/11/21
 * description: 修改规则请求参数
 */
@Data
public class RuleUpdateReqDto implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 规则组件GroupId
     */
    private String groupId;

    /**
     * 规则组件ArtifactId
     */
    private String artifactId;

    /**
     * 备注
     */
    private String note;

    /**
     * 规则内容修改来源（原有或新上传的）
     */
    private List<String> drlOriginList;

    /**
     * 原有规则文件内容列表
     */
    private List<String> drlList;

    /**
     * 上传的文件中的字符串
     */
    private List<String> files;

    /**
     * 所有规则的描述列表
     */
    private List<String> descriptionList;

}
