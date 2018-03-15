package com.virgo.finance.loan.rule.web.vo;

import lombok.Data;

/**
 * @author: zhulili1
 * date: 2017/10/28
 * description: Drl文件及描述Vo
 */
@Data
public class DrlFileVo {

    /**
     * drl文件内容
     */
    private String drl;

    /**
     * 描述
     */
    private String description;

    public DrlFileVo (String drl, String description) {
        this.drl = drl;
        this.description = description;
    }

}
