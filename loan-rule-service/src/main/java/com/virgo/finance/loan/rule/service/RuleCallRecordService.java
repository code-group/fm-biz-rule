package com.virgo.finance.loan.rule.service;

import com.virgo.finance.loan.rule.dao.domain.RuleCallRecord;
import com.virgo.finance.loan.rule.dao.mapper.RuleCallRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: zhull
 * date: 2017/11/6
 * description: 调用规则记录服务类
 */
@Slf4j
@Service
public class RuleCallRecordService {

    @Autowired
    private RuleCallRecordMapper ruleCallRecordMapper;

    /**
     * created by zhulili1, on 2017/11/6
     * description: 保存规则调用记录
     **/
    public void saveCallRecord(String requestParameter, String responseResult) {
        new Thread(() -> {
            // 日志前缀
            final String logPrefix = "保存规则调用记录, ";
            log.info("{}入参, {}, {}", logPrefix, requestParameter, responseResult);
            RuleCallRecord ruleCallRecord = new RuleCallRecord();
            ruleCallRecord.setRequestParameter(requestParameter);
            ruleCallRecord.setResponseResult(responseResult);
            ruleCallRecord.setCreationTime(new Date());
            log.info("{}{}", logPrefix, ruleCallRecord.toString());
            ruleCallRecordMapper.insertSelective(ruleCallRecord);
        }).start();
    }
}
