package com.leFinance.creditLoan.bizRule.interfaces.impl;

import com.leFinance.creditLoan.bizRule.bo.RuleVersionBo;
import com.leFinance.creditLoan.bizRule.dto.RuleReqDto;
import com.leFinance.creditLoan.bizRule.interfaces.RuleInterface;
import com.leFinance.creditLoan.bizRule.service.RuleCallService;
import data.Message;
import data.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.leFinance.creditLoan.bizRule.common.enums.SystemExceptionCode.INTERFACE_CALL_SERVICE;

/**
 * @author: zhulili1
 * @date: 2017/10/11
 * @description: 规则接口
 */
@Slf4j
@Service
public class RuleInterfaceImpl implements RuleInterface {

    @Autowired
    private RuleCallService ruleCallService;

    /**
     * created by zhulili1, on 2017/10/11
     * @param ruleReqDto
     * @Description: 调用规则判定
     */
    @Override
    public Message<Map<String, Object>> callRule(RuleReqDto ruleReqDto) {
        // 日志前缀
        final String logPrefix = "调用规则接口, ";
        try {
            log.info("{}传入参数, {}", logPrefix, ruleReqDto);
            RuleVersionBo ruleVersionBo = new RuleVersionBo();
            BeanUtils.copyProperties(ruleReqDto, ruleVersionBo);
            Map<String, Object> dataMap = ruleReqDto.getDataMap();
            ruleCallService.callRule(ruleVersionBo, dataMap);
            log.info("{}结果{}", logPrefix, dataMap);
            return Messages.success(dataMap);
        } catch (Exception e) {
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            return Messages.failed(INTERFACE_CALL_SERVICE.getCode(),INTERFACE_CALL_SERVICE.getMessage()+e.getMessage());
        }
    }
}
