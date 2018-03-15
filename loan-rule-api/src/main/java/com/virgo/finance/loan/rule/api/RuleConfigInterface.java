package com.virgo.finance.loan.rule.api;

import com.virgo.finance.loan.common.data.Message;
import com.virgo.finance.loan.rule.api.dto.*;

/**
 * @author: zhull
 * <P>date: 2017/11/28</P>
 * <P>description: 规则配置接口</P>
 */
public interface RuleConfigInterface {

    /**
     * created by zhull, on 2017/11/14
     * description: groupId, artifactId验重接口
     **/
    Message<Integer> checkDuplication(String groupId, String artifactId);


    /**
     * created by zhull, on 2017/11/16
     * description: 创建规则
     **/
    Message<String> createRule(RuleCreateReqDto ruleCreateReqDto);

    /**
     * created by zhull, on 2017/11/17
     * description: 获取规则信息列表
     **/
    Message<RuleInfoQueryResPageDto> getRuleInfoList(RuleInfoQueryReqDto ruleInfoQueryReqDto);

    /**
     * created by zhull, on 2017/11/20
     * description: 查询规则信息详情
     **/
    Message<RuleDetailResDto> getRuleDetail(String ruleNo);

    /**
     * created by zhull, on 2017/11/21
     * description: 修改规则信息
     **/
    Message<String> updateRule(RuleUpdateReqDto ruleUpdateReqDto);

    /**
     * created by zhull, on 2017/11/21
     * description: 调用规则（调试用）
     **/
    Message<RuleCallResDto> callRuleForTest(RuleCallReqDto ruleCallReqDto);

    /**
     * created by zhull, on 2017/11/22
     * description: 启用规则
     **/
    Message<Boolean> loadRule(RuleLoadReqDto ruleLoadReqDto);

    /**
     * created by zhull, on 2017/11/22
     * description: 禁用规则
     **/
    Message<Boolean> unloadRule(String ruleNo);
}
