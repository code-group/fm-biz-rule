package com.virgo.finance.loan.rule.web.interfaces.impl;

import com.virgo.finance.loan.common.data.Message;
import com.virgo.finance.loan.common.data.Messages;
import com.virgo.finance.loan.rule.api.RuleInterface;
import com.virgo.finance.loan.rule.api.dto.RuleReqDto;
import com.virgo.finance.loan.rule.api.dto.RuleResDto;
import com.virgo.finance.loan.rule.common.enums.RuleEnums;
import com.virgo.finance.loan.rule.dao.bo.RuleVersionBo;
import com.virgo.finance.loan.rule.service.RuleCallRecordService;
import com.virgo.finance.loan.rule.service.RuleCallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.virgo.finance.loan.rule.common.enums.SystemExceptionCode.INTERFACE_CALL_SERVICE;

/**
 * @author: zhulili1
 * date: 2017/10/11
 * description: 规则接口, 调用规则平台方法
 */
@Slf4j
@Service
public class RuleInterfaceImpl implements RuleInterface {

    @Autowired
    private RuleCallService ruleCallService;

    @Autowired
    private RuleCallRecordService ruleCallRecordService;

    /**
     * created by zhulili1, on 2017/10/11
     * update on 2017/10/26
     * @param ruleReqDto
     * description: 调用规则
     */
    @Override
    public Message<RuleResDto> callRule(RuleReqDto ruleReqDto) {
        // 日志前缀
        final String logPrefix = "调用规则接口, ";
        try {
            log.info("{}传入参数, {}", logPrefix, ruleReqDto);
            // 记录调用记录
            ruleCallRecordService.saveCallRecord(ruleReqDto.toString(), null);

            RuleVersionBo ruleVersionBo = new RuleVersionBo();
            BeanUtils.copyProperties(ruleReqDto, ruleVersionBo);
            Map<String, Object> dataMap = ruleReqDto.getDataMap();
            dataMap = dataMap == null ? new HashMap<>() : dataMap;
            // 调用规则
            int result = ruleCallService.callRule(ruleVersionBo, dataMap);
            // 设置返回参数
            RuleResDto ruleResDto = new RuleResDto();
            ruleResDto.setCode(result);
            ruleResDto.setMessage(RuleEnums.RuleCode.messageOf(result));
            ruleResDto.setResult(dataMap);
            log.info("{}结果{}", logPrefix, ruleResDto.toString());
            // 记录调用记录
            ruleCallRecordService.saveCallRecord(ruleReqDto.toString(), ruleResDto.toString());

            return Messages.success(ruleResDto);
        } catch (Exception e) {
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            ruleCallRecordService.saveCallRecord(ruleReqDto.toString(), e.getMessage());
            return Messages.failed(INTERFACE_CALL_SERVICE.getCode(),INTERFACE_CALL_SERVICE.getMessage()+e.getMessage());
        }
    }


}
