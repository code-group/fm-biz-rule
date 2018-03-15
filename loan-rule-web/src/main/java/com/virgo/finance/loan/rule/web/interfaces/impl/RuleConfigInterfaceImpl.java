package com.virgo.finance.loan.rule.web.interfaces.impl;

import com.virgo.finance.loan.common.data.Message;
import com.virgo.finance.loan.common.data.Messages;
import com.virgo.finance.loan.rule.api.RuleConfigInterface;
import com.virgo.finance.loan.rule.api.dto.*;
import com.virgo.finance.loan.rule.dao.bo.RuleCreateBo;
import com.virgo.finance.loan.rule.dao.bo.RuleVersionBo;
import com.virgo.finance.loan.rule.dao.mapper.RuleInfoMapper;
import com.virgo.finance.loan.rule.service.RuleCallService;
import com.virgo.finance.loan.rule.service.utils.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.virgo.finance.loan.rule.common.enums.SystemExceptionCode.INTERFACE_CALL_SERVICE;
import static com.virgo.finance.loan.rule.common.enums.SystemExceptionCode.SYSTEM_INTERNAL_EXCEPTION;

/**
 * @author: zhull
 * <P>date: 2017/11/28</P>
 * <P>description: 管理规则</P>
 */
@Slf4j
@Service
public class RuleConfigInterfaceImpl implements RuleConfigInterface {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private RuleInfoMapper ruleInfoMapper;

    @Autowired
    private RuleCallService ruleCallService;

    /**
     * created by zhull, on 2017/11/14
     *
     * @param groupId
     * @param artifactId
     * description: groupId, artifactId验重接口
     */
    @Override
    public Message<Integer> checkDuplication(String groupId, String artifactId) {
        // 日志前缀
        final String logPrefix = "groupId, artifactId验重接口, ";
        try {
            int count = ruleInfoMapper.countRecordByGroupArtifactId(groupId, artifactId);
            return Messages.success(new Integer(count));
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            return Messages.failed(SYSTEM_INTERNAL_EXCEPTION.getCode(), SYSTEM_INTERNAL_EXCEPTION.getMessage());
        }
    }

    /**
     * created by zhull, on 2017/11/16
     *
     * @param ruleCreateReqDto
     * description: 创建规则
     */
    @Override
    public Message<String> createRule(RuleCreateReqDto ruleCreateReqDto) {
        // 日志前缀
        final String logPrefix = "创建规则, ";
        try {
            log.info("{}入参{}", logPrefix, ruleCreateReqDto.toString());
            RuleCreateBo ruleCreateBo = new RuleCreateBo();
            ruleCreateBo.setGroupId(ruleCreateReqDto.getGroupId());
            ruleCreateBo.setArtifactId(ruleCreateReqDto.getArtifactId());
            ruleCreateBo.setNote(ruleCreateReqDto.getNote());
            String ruleNo = ruleService.createRule(ruleCreateBo, ruleCreateReqDto.getDrlList(), ruleCreateReqDto.getDescriptionList());
            log.info("{}生成规则编号{}", logPrefix, ruleNo);
            return Messages.success(ruleNo);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            return Messages.failed(INTERFACE_CALL_SERVICE.getCode(),INTERFACE_CALL_SERVICE.getMessage()+e.getMessage());
        }
    }

    /**
     * created by zhull, on 2017/11/17
     *
     * @param ruleInfoQueryReqDto
     * description: 获取规则信息列表
     */
    @Override
    public Message<RuleInfoQueryResPageDto> getRuleInfoList(RuleInfoQueryReqDto ruleInfoQueryReqDto) {
        // 日志前缀
        final String logPrefix = "获取规则信息列表, ";
        try {
            log.info("{}入参, {}", logPrefix, ruleInfoQueryReqDto.toString());
            RuleInfoQueryResPageDto ruleInfoQueryResPageDto = new RuleInfoQueryResPageDto();
            // 查询记录的总数
            int totalCount = ruleService.countRuleInfoByCondition(ruleInfoQueryReqDto.getGroupId(),
                    ruleInfoQueryReqDto.getArtifactId());
            ruleInfoQueryResPageDto.setTotalCount(totalCount);
            // 查询请求的数据
            List<RuleInfoQueryResDto> ruleInfoQueryResDtoList = ruleService.queryRuleInfoPage(ruleInfoQueryReqDto);
            ruleInfoQueryResPageDto.setRuleInfoQueryResDtoList(ruleInfoQueryResDtoList);
            log.info("{}返参{}", logPrefix, ruleInfoQueryResPageDto.toString());
            return Messages.success(ruleInfoQueryResPageDto);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            return Messages.failed(INTERFACE_CALL_SERVICE.getCode(),INTERFACE_CALL_SERVICE.getMessage()+e.getMessage());
        }
    }

    /**
     * created by zhull, on 2017/11/20
     * description: 查询规则信息详情
     *
     * @param ruleNo
     */
    @Override
    public Message<RuleDetailResDto> getRuleDetail(String ruleNo) {
        // 日志前缀
        final String logPrefix = "查询规则信息详情, ";
        try {
            log.info("{}入参, ruleNo={}", logPrefix, ruleNo);
            // 查询规则详情
            RuleDetailResDto ruleDetailResDto = ruleService.getRuleDetail(ruleNo);
            log.info("{}返回结果,{}", logPrefix, ruleDetailResDto.toString());
            return Messages.success(ruleDetailResDto);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            return Messages.failed(INTERFACE_CALL_SERVICE.getCode(),INTERFACE_CALL_SERVICE.getMessage()+e.getMessage());
        }
    }

    /**
     * created by zhull, on 2017/11/21
     * description: 修改规则信息
     *
     * @param ruleUpdateReqDto
     */
    @Override
    public Message<String> updateRule(RuleUpdateReqDto ruleUpdateReqDto) {
        // 日志前缀
        final String logPrefix = "修改规则信息, ";

        try {
            log.info("{}入参,{}", logPrefix, ruleUpdateReqDto.toString());
            RuleVersionBo ruleVersionBo = new RuleVersionBo(ruleUpdateReqDto.getGroupId(),
                    ruleUpdateReqDto.getArtifactId(), "", ruleUpdateReqDto.getNote());
            String ruleNo = ruleService.updateDrl(ruleVersionBo, ruleUpdateReqDto.getDescriptionList(),
                    ruleUpdateReqDto.getDrlOriginList(), ruleUpdateReqDto.getDrlList(), ruleUpdateReqDto.getFiles());
            log.info("{}返回结果, ruleNo={}", logPrefix, ruleNo);
            return Messages.success(ruleNo);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            return Messages.failed(INTERFACE_CALL_SERVICE.getCode(),INTERFACE_CALL_SERVICE.getMessage()+e.getMessage());
        }
    }

    /**
     * created by zhull, on 2017/11/21
     * description: 调用规则（调试用）
     *
     * @param ruleCallReqDto
     */
    @Override
    public Message<RuleCallResDto> callRuleForTest(RuleCallReqDto ruleCallReqDto) {
        // 日志前缀
        final String logPrefix = "调用规则（调试用）, ";

        try {
            log.info("{}入参, {}", logPrefix, ruleCallReqDto.toString());
            HashMap<String, Object> dataMap = null;
            if (ruleCallReqDto.getTypeList() != null) {
                // 组装数据
                dataMap = (HashMap) ruleService.createDataMap(ruleCallReqDto.getTypeList(), ruleCallReqDto.getKeyList(),
                        ruleCallReqDto.getValueList());
            }
            RuleCallResDto ruleCallResDto = new RuleCallResDto();
            // 请求参数
            Map<String, Object> dataReq = dataMap == null ? null : (HashMap) dataMap.clone();
            ruleCallResDto.setDataReq(dataReq);
            // 调用规则
            RuleVersionBo ruleVersionBo = new RuleVersionBo(ruleCallReqDto.getGroupId(), ruleCallReqDto.getArtifactId(),
                    ruleCallReqDto.getVersion());
            dataMap = (HashMap<String, Object>) ruleCallService.testCallRule(ruleVersionBo, dataMap);
            // 设置返回结果
            ruleCallResDto.setResult(dataMap);
            log.info("{}返回结果, {}", logPrefix, ruleCallResDto.toString());
            return Messages.success(ruleCallResDto);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            return Messages.failed(INTERFACE_CALL_SERVICE.getCode(),INTERFACE_CALL_SERVICE.getMessage()+e.getMessage());
        }
    }

    /**
     * created by zhull, on 2017/11/22
     * description: 启用规则
     *              启用某一版本的规则, 同时禁用同一规则的其他版本。
     * @param ruleLoadReqDto
     */
    @Override
    public Message<Boolean> loadRule(RuleLoadReqDto ruleLoadReqDto) {
        // 日志前缀
        final String logPrefix = "启用规则, ";
        log.info("{}入参, {}", logPrefix, ruleLoadReqDto.toString());
        try {
            ruleService.loadAndUnloadRule(ruleLoadReqDto.getGroupId(), ruleLoadReqDto.getArtifactId(),
                    ruleLoadReqDto.getVersion());
            return Messages.success(Boolean.TRUE);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            return Messages.failed(INTERFACE_CALL_SERVICE.getCode(),INTERFACE_CALL_SERVICE.getMessage()+e.getMessage());
        }
    }

    /**
     * created by zhull, on 2017/11/22
     * description: 禁用规则
     *
     * @param ruleNo
     */
    @Override
    public Message<Boolean> unloadRule(String ruleNo) {
        // 日志前缀
        final String logPrefix = "禁用规则, ";
        log.info("{}入参, rule = {}", logPrefix, ruleNo);
        try {
            ruleService.unloadRule(ruleNo);
            return Messages.success(Boolean.TRUE);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            return Messages.failed(INTERFACE_CALL_SERVICE.getCode(),INTERFACE_CALL_SERVICE.getMessage()+e.getMessage());
        }
    }

}
