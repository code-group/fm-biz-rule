package com.virgo.finance.loan.rule.api;

import com.virgo.finance.loan.common.data.Message;
import com.virgo.finance.loan.rule.api.dto.RuleReqDto;
import com.virgo.finance.loan.rule.api.dto.RuleResDto;

/**
 * @author: zhulili1
 * date: 2017/10/11
 * description: 规则引擎对外开放的调用接口
 */
public interface RuleInterface {

    /**
     * created by zhulili1, on 2017/10/11,
     * update on 2017/10/26
     * description: 调用规则参数接收入口
     **/
    Message<RuleResDto> callRule(RuleReqDto ruleReqDto);

}
