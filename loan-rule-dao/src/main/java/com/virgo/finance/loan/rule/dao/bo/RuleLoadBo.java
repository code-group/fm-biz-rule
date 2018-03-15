package com.virgo.finance.loan.rule.dao.bo;

import lombok.Data;

/**
 * @author: zhulili1
 * date: 2017/10/16
 * description: 加载规则Bo
 */

@Data
public class RuleLoadBo {

    /**
     * KieModule内容的字符串
     */
    private String kmodule;

    /**
     * KieContainer名称
     */
    private String containerName;

}
