package com.virgo.finance.loan.rule.dao.mapper;

import com.virgo.finance.loan.rule.dao.domain.RuleCallRecord;

public interface RuleCallRecordMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(RuleCallRecord record);

    int insertSelective(RuleCallRecord record);

    RuleCallRecord selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(RuleCallRecord record);

    int updateByPrimaryKey(RuleCallRecord record);
}