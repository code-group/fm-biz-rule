package com.virgo.finance.loan.rule.service.utils;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.virgo.finance.loan.rule.api.dto.RuleDetailDto;
import com.virgo.finance.loan.rule.api.dto.RuleDetailResDto;
import com.virgo.finance.loan.rule.api.dto.RuleInfoQueryReqDto;
import com.virgo.finance.loan.rule.api.dto.RuleInfoQueryResDto;
import com.virgo.finance.loan.rule.common.enums.RuleEnums;
import com.virgo.finance.loan.rule.common.utils.KieUtil;
import com.virgo.finance.loan.rule.dao.bo.RuleCreateBo;
import com.virgo.finance.loan.rule.dao.bo.RuleVersionBo;
import com.virgo.finance.loan.rule.dao.domain.RuleInfo;
import com.virgo.finance.loan.rule.dao.mapper.RuleInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.virgo.finance.loan.rule.common.enums.RuleEnums.Status.RULE_STATUS_FORBIDDING;
import static com.virgo.finance.loan.rule.common.enums.RuleEnums.Version.INITIAL_VERSION;
import static com.virgo.finance.loan.rule.common.enums.RuleEnums.Version.WORKING_VERSION;

/**
 * @author: zhulili1
 * date: 2017/10/16
 * description: 重新加载规则服务类
 */
@Slf4j
@Service
public class RuleService {

    @Autowired
    private RuleLoadService ruleLoadService;

    @Autowired
    private RuleInfoMapper ruleInfoMapper;

    // 规则编号ruleNo生成格式
    private final String ruleNoFormatter = "%03d";

    /**
     * created by zhulili1, on 2017/10/16
     * description: 重新加载规则
     * @param ruleVersion
     */
    public void reloadRule(RuleVersionBo ruleVersion){
        // 日志前缀
        final String logPrefix = "重新加载规则, ";
        try{
            log.info("{}传入参数{}", logPrefix, ruleVersion);
            // 查询 drl
            String[] drls = ruleLoadService.getDrlArray(ruleVersion);
            // 重新加载规则
            KieUtil.loadRule(ruleVersion.getGroupId(), ruleVersion.getArtifactId(), ruleVersion.getVersion(), drls);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常" + e.getMessage());
        }

    }

    /**
     * created by zhulili1, on 2017/10/18
     * description: 为规则升级版本标识（仅升级版本号, 原规则内容不变）
     * @param oldVersion
     * @param newVersion
     */
    public void upgradeRuleVersion(RuleVersionBo oldVersion, RuleVersionBo newVersion){
        // 日志前缀
        final String logPrefix = "为规则升级版本标识, ";
        try{
            log.info("{}传入参数, oldVersion={}, newVersion={}", logPrefix, oldVersion.toString(), newVersion.toString());
            List<RuleInfo> ruleInfoList = ruleInfoMapper.listSelectRuleInfoByRuleVersion(oldVersion);
            log.info("{}查询旧版本规则信息, 返回结果{}", logPrefix, ruleInfoList);
            if(ruleInfoList == null || ruleInfoList.size() == 0){
                throw new RuntimeException("无法升级版本号：查询旧版本规则信息, 返回结果为空");
            }
            updateVersion(ruleInfoList, newVersion);
            log.info("{}新版本RuleInfo为: {}", logPrefix, ruleInfoList.toString());
            ruleInfoMapper.batchInsert(ruleInfoList);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常" + e.getMessage());
        }
    }

    /**
     * created by zhulili1, on 2017/10/18
     * description: 修改ruleInfoList
     * @param ruleInfoList
     * @param newVersion
     */
    private void updateVersion(List<RuleInfo> ruleInfoList, RuleVersionBo newVersion) {
        for (RuleInfo ruleInfo : ruleInfoList) {
            ruleInfo.setGroupId(newVersion.getGroupId());
            ruleInfo.setArtifactId(newVersion.getArtifactId());
            ruleInfo.setVersion(newVersion.getVersion());
            ruleInfo.setCreateTime(new Date());
            ruleInfo.setUpdateTime(null);
        }
    }
    /**
     * created by zhulili1, on 2017/10/18
     * description: 创建规则
     * @param ruleCreateBo
     */
    public void createRuleInfo(RuleCreateBo ruleCreateBo){
        // 日志前缀
        final String logPrefix = "创建规则, ";

        try{
            log.info("{}传入参数, {}", logPrefix, ruleCreateBo.toString());
            RuleInfo ruleInfo = new RuleInfo();
            BeanUtils.copyProperties(ruleCreateBo, ruleInfo);
            ruleInfo.setCreateTime(new Date());
            log.info("{}插入规则记录, {}", logPrefix, ruleInfo);
            ruleInfoMapper.insertSelective(ruleInfo);
        } catch(Exception e){
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * created by zhulili1, on 2017/10/23
     * description: 查询规则
     * @param groupId
     * @param artifactId
     * @return
     */
    public List<RuleVersionBo> queryRuleVersionList(String groupId, String artifactId){
        // 日志前缀
        final String logPrefix = "查询规则, ";

        try{
            log.info("{}传入参数, {}, {}", logPrefix, groupId, artifactId);
            RuleVersionBo ruleCreateBo = new RuleVersionBo();
            ruleCreateBo.setGroupId(groupId);
            ruleCreateBo.setArtifactId(artifactId);
            List<RuleInfo> ruleInfoList = ruleInfoMapper.listSelectRuleVersion(ruleCreateBo);
            List<RuleVersionBo> ruleVersionBoList = Lists.transform(ruleInfoList, new Function<RuleInfo, RuleVersionBo>() {
                @Override
                public RuleVersionBo apply(RuleInfo ruleInfo){
                    RuleVersionBo ruleVersionBo = new RuleVersionBo();
                    BeanUtils.copyProperties(ruleInfo, ruleVersionBo);
                    return ruleVersionBo;
                }
            });
            return ruleVersionBoList;
        } catch(Exception e) {
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
        }
        return null;
    }

    /**
     * created by zhulili1, on 2017/10/23
     * description: 查询规则信息列表
     **/
    public List<RuleInfo> queryRuleInfoList(String groupId, String artifactId, String version) {
        // 日志前缀
        final String logPrefix = "查询规则信息列表, ";

        try{
            log.info("{}传入参数, {}, {}, {}", logPrefix, groupId, artifactId, version);
            RuleVersionBo ruleVersionBo = new RuleVersionBo(groupId, artifactId, version);
            List<RuleInfo> ruleInfoList = ruleInfoMapper.listSelectRuleInfoByRuleVersion(ruleVersionBo);
//            for (RuleInfo ruleInfo : ruleInfoList) {
//                // 仅返回文件内容的前30个字符
//                if (ruleInfo.getDrl().length() > 30){
//                    ruleInfo.setDrl(ruleInfo.getDrl().substring(0, 30) + "...");
//                }
//            }
            log.info("{}结果: {}", logPrefix, ruleInfoList.toString());
            return ruleInfoList;
        } catch(Exception e) {
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
        }
        return null;
    }

    /**
     * created by zhulili1, on 2017/10/23
     * description: 查询规则信息
     **/
    public RuleInfo queryRuleInfo(String pid) {
        // 日志前缀
        final String logPrefix = "查询规则信息, ";

        try{
            log.info("{}传入参数, pid = {}", logPrefix, pid);
            RuleInfo ruleInfo = ruleInfoMapper.selectByPrimaryKey(Integer.parseInt(pid));
            log.info("{}结果: {}", logPrefix, ruleInfo.toString());
            return ruleInfo;
        } catch(Exception e) {
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
        }
        return null;
    }

    /**
     * created by zhulili1, on 2017/10/23
     * description: 更新规则信息
     **/
    public void updateDrl(RuleInfo ruleInfo) {
        // 日志前缀
        final String logPrefix = "更新规则信息, ";

        try{
            log.info("{}传入参数, {}", logPrefix, ruleInfo);
            ruleInfo.setUpdateTime(new Date());
            ruleInfoMapper.updateByPrimaryKeySelective(ruleInfo);
            log.info("{}结果: {}", logPrefix, ruleInfo.toString());
        } catch(Exception e) {
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
        }
    }
    /**
     * created by zhulili1, on 2017/10/23
     * description: 删除一组规则信息
     **/
    public void delRuleByVersion(RuleVersionBo ruleVersionBo) {
        // 日志前缀
        final String logPrefix = "删除一组规则信息, ";

        try{
            log.info("{}传入参数, {}", logPrefix, ruleVersionBo.toString());
            ruleInfoMapper.delRuleByVersion(ruleVersionBo);
        } catch(Exception e) {
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常" + e.getMessage());
        }
    }
    /**
     * created by zhulili1, on 2017/10/23
     * description: 删除一条规则文件
     **/
    public void delDrl(String pid) {
        // 日志前缀
        final String logPrefix = "删除一条规则文件, ";

        try{
            log.info("{}传入参数, pid = {}", logPrefix, pid);
            ruleInfoMapper.deleteByPrimaryKey(Integer.parseInt(pid));
        } catch(Exception e) {
            log.error("{}异常, {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常" + e.getMessage());
        }
    }
    /**
     * created by zhulili1, on 2017/10/25
     * description: 创建并加载规则
     **/
    @Transactional(rollbackFor=Exception.class)
    public void createAndLoadRule(RuleCreateBo ruleCreateBo){
        createRuleInfo(ruleCreateBo);
        ruleLoadService.loadRule(ruleCreateBo.getGroupId(), ruleCreateBo.getArtifactId(), ruleCreateBo.getVersion());
    }

    /**
     * created by zhulili1, on 2017/10/25
     * description: 修改drl并加载规则
     **/
    @Transactional(rollbackFor=Exception.class)
    public void updateDrlAndLoadRule(RuleInfo ruleInfo){
        updateDrl(ruleInfo);
        ruleLoadService.loadRule(ruleInfo.getGroupId(), ruleInfo.getArtifactId(), ruleInfo.getVersion());
    }

    /**
     * created by zhulili1, on 2017/10/26
     * description: 启用某一版本的规则, 同时禁用同一规则的其他版本。
     *
     * update on 2017/11/22
     * 修改返回参数
     **/
    @Transactional(rollbackFor=Exception.class)
    public void loadAndUnloadRule(String groupId, String artifactId, String version) {
        log.info("启用某一版本的规则, 同时禁用同一规则的其他版本, 入参: groupId={}, artifactId={}, version={}",
                groupId, artifactId, version);
        // 修改版本状态
        ruleInfoMapper.updateRuleStatusByVersion(groupId, artifactId, version);
        // 加载规则
        ruleLoadService.loadRule(groupId, artifactId, WORKING_VERSION.code);
    }

    /**
     * created by zhulili1, on 2017/11/22
     * description: 根据ruleNo，禁用规则
     **/
    @Transactional(rollbackFor=Exception.class)
    public void unloadRule(String ruleNo) {
        // 日志前缀
        final String logPrefix = "根据ruleNo，禁用规则, ";
        try {
            log.info("根据ruleNo，禁用规则, 入参: ruleNo={}", ruleNo);
            // 修改版本状态
            ruleInfoMapper.updateRuleStatusByRuleNo(ruleNo);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常: " + e.getMessage());
        }
    }

    /**
     * created by zhulili1, on 2017/10/27
     * description: 禁用规则
     **/
    public void unloadRule(RuleVersionBo ruleVersionBo) {
        // 日志前缀
        final String logPrefix = "禁用规则, ";
        try {
            log.info("{}入参, {}", logPrefix, ruleVersionBo);
            ruleInfoMapper.updateStatusToUnload(ruleVersionBo);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常: " + e.getMessage());
        }
    }

    /**
     * created by zhulili1, on 2017/10/28
     * description: 创建规则
     * @update on 2017/11/16
     **/
    public String createRule(RuleCreateBo ruleCreateBo, List<String> drlList, List<String> descriptionList) {
        // 日志前缀
        final String logPrefix = "创建规则, ";
        log.info("{}入参: {}, drlList.size={}, {}", logPrefix, ruleCreateBo, drlList.size(), descriptionList.toString());

        try {
            // 生成规则编号
            String ruleNo = generateRuleNo();
            List<RuleInfo> ruleInfoList = new ArrayList<>();
            // 封装规则信息列表
            for (int i = 0; i < drlList.size(); ++i) {
                RuleInfo ruleInfo = new RuleInfo();
                ruleInfo.setRuleNo(ruleNo);
                ruleInfo.setGroupId(ruleCreateBo.getGroupId());
                ruleInfo.setArtifactId(ruleCreateBo.getArtifactId());
                ruleInfo.setNote(ruleCreateBo.getNote());
                ruleInfo.setVersion(INITIAL_VERSION.code);
                ruleInfo.setDrl(drlList.get(i));
                if (!descriptionList.isEmpty()) {
                    ruleInfo.setDescription(descriptionList.get(i));
                }
                ruleInfo.setStatus(RULE_STATUS_FORBIDDING.code);
                ruleInfo.setCreateTime(new Date());
                ruleInfoList.add(ruleInfo);
            }
            log.info("{}批量插入: {}", logPrefix, ruleInfoList.toString());
            ruleInfoMapper.batchInsert(ruleInfoList);
            return ruleNo;
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常: " + e.getMessage());
        }
    }

    /**
     * created by zhulili1, on 2017/10/30
     * description: 修改规则
     * update on 2017/11/21
     * description: 改为返回规则编号, 改变入参files为字符串列表
     **/
    public String updateDrl(RuleVersionBo ruleVersionBo, List<String> descriptionList,
                            List<String> drlOriginList, List<String> drlList, List<String> files) {
        // 日志前缀
        final String logPrefix = "修改规则, ";
        log.info("{}传入参数：{}, {}, drlOriginList.size={}, drlList.size={}", logPrefix, ruleVersionBo.toString(),
                descriptionList.toString(), drlOriginList.size(), drlList.size());
        try {
            List<RuleInfo> ruleInfoList = new ArrayList<>();
            // 新规则编号
            ruleVersionBo.setRuleNo(generateRuleNo());
            // 新版本号
            String newVersion = String.valueOf(Integer.valueOf(ruleInfoMapper.selectMaxVersion(ruleVersionBo))+ 1);
            ruleVersionBo.setVersion(newVersion);
            // 根据drlOriginList修改原有文件
            for (int i = 0; i < drlOriginList.size(); i++) {
                String description = descriptionList.size() == 0 ? "" : descriptionList.get(i);
                if (RuleEnums.Origin.RULE_FROM_STRING.type.equals(drlOriginList.get(i))){
                    // 修改内容来自字符串
                    ruleInfoList.add(createRuleInfo(ruleVersionBo, description,null, drlList.get(i)));
                } else {
                    // 修改内容来自文件
                    ruleInfoList.add(createRuleInfo(ruleVersionBo, description, files.get(i), null));
                }
            }
            // 创建新加规则
            for (int i = drlOriginList.size(); i < files.size(); i++) {
                ruleInfoList.add(createRuleInfo(ruleVersionBo, descriptionList.get(i), files.get(i), null));
            }
            log.info("{}批量插入: {}", logPrefix, ruleInfoList.toString());
            ruleInfoMapper.batchInsert(ruleInfoList);
            // 返回生成的规则编号
            return ruleVersionBo.getRuleNo();
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException("修改规则异常：" + e.getMessage());
        }

    }

    /**
     * created by zhulili1, on 2017/10/31
     * description: 创建RuleInfo对象
     **/
    private RuleInfo createRuleInfo(RuleVersionBo ruleVersionBo, String description, String file, String drl) {
        // 日志前缀
        final String logPrefix = "创建RuleInfo对象, ";
        try {
            RuleInfo ruleInfo = new RuleInfo();
            BeanUtils.copyProperties(ruleVersionBo, ruleInfo);
            ruleInfo.setDescription(description);
            if (!StringUtils.isEmpty(file)) {
                drl = file;
            }
            ruleInfo.setDrl(drl);
            ruleInfo.setStatus(RULE_STATUS_FORBIDDING.code);
            ruleInfo.setCreateTime(new Date());

            return ruleInfo;
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常: " + e.getMessage());
        }
    }

    /**
     * created by zhulili1, on 2017/10/31
     * description: 创建JSON字符串
     **/
    public Map<String, Object> createDataMap(List<String> typeList, List<String> keyList, List<String> valueList) {
        // 日志前缀
        final String logPrefix = "创建JSON字符串, ";
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        try {
            log.info("{}, 入参, {}, {}, {}", logPrefix, typeList.toString(), keyList.toString(), valueList.toString());
            for (int i = 0; i < typeList.size(); i++) {
                sb.append("\"");
                sb.append(keyList.get(i));
                sb.append("\":");
                String value = valueList.size() == 0 ? null : valueList.get(i);
                if (RuleEnums.DataType.DATA_TYPE_STRING.type.equals(typeList.get(i))) {
                    if (value != null) {
                        // 字符串类型
                        sb.append("\"");
                        sb.append(value);
                        sb.append("\"");
                    }
                } else {
                    // 数字类型
                    sb.append(value);
                }
                if (i != typeList.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("}");
            log.info("{}入参转为JSON字符串: {}", logPrefix, sb.toString());
            Map<String, Object> dataMap = (Map<String, Object>) JSONUtils.parse(sb.toString());
            log.info("{}, 返参, {}", logPrefix, dataMap.toString());
            return dataMap;
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常: " + e.getMessage());
        }
    }

    /**
     * created by zhull, on 2017/11/17
     * description: 根据条件获取规则信息总数量
     * @param groupId
     * @param artifactId
     * @return
     */
    public int countRuleInfoByCondition(String groupId, String artifactId) {
        // 日志前缀
        final String logPrefix = "根据条件获取规则信息总数量, ";
        try {
            log.info("{}入参：groupId={}, artifactId={}", logPrefix, groupId, artifactId);
            int totalCount = ruleInfoMapper.countDistinctVersionByGroupArtifactId(groupId, artifactId);
            log.info("{}totalCount={}", logPrefix, totalCount);
            return totalCount;
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常: " + e.getMessage());
        }
    }
    /**
     * created by zhull, on 2017/11/17
     * description: 根据条件获取规则信息分页数据
     * @param ruleInfoQueryReqDto
     * @return
     */
    public List<RuleInfoQueryResDto> queryRuleInfoPage(RuleInfoQueryReqDto ruleInfoQueryReqDto) {
        // 日志前缀
        final String logPrefix = "根据条件获取规则信息分页数据, ";
        try {
            log.info("{}入参：{}", logPrefix, ruleInfoQueryReqDto);
            // 查询起始记录位置
            int startIndex = (ruleInfoQueryReqDto.getCurrentPageNo()-1) * ruleInfoQueryReqDto.getPageSize();
            // 查询分页数据
            List<RuleInfo> ruleInfoList = ruleInfoMapper.selectRuleInfoPage(ruleInfoQueryReqDto.getGroupId(),
                    ruleInfoQueryReqDto.getArtifactId(), startIndex, ruleInfoQueryReqDto.getPageSize());
            List<RuleInfoQueryResDto> ruleInfoQueryResDtoList = Lists.transform(ruleInfoList, ruleInfo -> {
                RuleInfoQueryResDto ruleInfoQueryResDto = new RuleInfoQueryResDto();
                BeanUtils.copyProperties(ruleInfo, ruleInfoQueryResDto);
                return ruleInfoQueryResDto;
            });
            log.info("{}生成分页数据, {}", logPrefix, ruleInfoList.toString());
            return ruleInfoQueryResDtoList;
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常: " + e.getMessage());
        }
    }

    /**
     * created by zhull, on 2017/11/20
     * description: 根据ruleNo查询规则详情
     **/
    public RuleDetailResDto getRuleDetail(String ruleNo) {
        // 日志前缀
        final String logPrefix = "根据ruleNo查询规则详情, ";
        try {
            log.info("{}入参, ruleNo={}", logPrefix, ruleNo);
            RuleDetailResDto ruleDetailResDto = new RuleDetailResDto();
            // 查询规则内容列表
            List<RuleInfo> ruleInfoList = ruleInfoMapper.getRuleDetailByRuleNo(ruleNo);
            List<RuleDetailDto> ruleDetailDtoList = Lists.transform(ruleInfoList, ruleInfo -> {
                RuleDetailDto ruleDetailDto = new RuleDetailDto();
                BeanUtils.copyProperties(ruleInfo, ruleDetailDto);
                return ruleDetailDto;
            });
            // 设置规则内容列表
            ruleDetailResDto.setRuleInfoList(ruleDetailDtoList);
            // 设置版本、备注等信息
            BeanUtils.copyProperties(ruleInfoList.get(0), ruleDetailResDto);
            log.info("{}返参,{}", logPrefix, ruleDetailResDto.toString());
            return ruleDetailResDto;
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常: " + e.getMessage());
        }

    }

    /**
     * created by zhull, on 2017/11/16
     * description: 生成规则编号=时间戳+pid+1
     **/
    private String generateRuleNo() {
        String no;
        // 获取时间戳
        synchronized (RuleService.class) {
            no = String.valueOf(System.currentTimeMillis());
        }
        try {
            // 查询最大pid, 并加1
            int nextPid = ruleInfoMapper.getMaxPid() + 1;
            no += String.format(ruleNoFormatter, nextPid);
        } catch(Exception e){
            log.error("生成规则编号异常：{}", e.getMessage(), e);
            throw new RuntimeException("生成规则编号异常");
        }
        return no;
    }
}
