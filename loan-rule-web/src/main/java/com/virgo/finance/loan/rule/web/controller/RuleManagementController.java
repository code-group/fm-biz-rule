package com.virgo.finance.loan.rule.web.controller;

import com.google.common.collect.Lists;
import com.virgo.finance.loan.rule.dao.bo.RuleCreateBo;
import com.virgo.finance.loan.rule.dao.bo.RuleVersionBo;
import com.virgo.finance.loan.rule.dao.domain.RuleInfo;
import com.virgo.finance.loan.rule.dao.mapper.RuleInfoMapper;
import com.virgo.finance.loan.rule.service.RuleCallService;
import com.virgo.finance.loan.rule.service.utils.RuleLoadService;
import com.virgo.finance.loan.rule.service.utils.RuleService;
import com.virgo.finance.loan.rule.web.vo.RuleCreateVo;
import com.virgo.finance.loan.rule.web.vo.RuleDetailQueryVo;
import com.virgo.finance.loan.rule.web.vo.RuleUpdateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: zhulili1
 * date: 2017/10/17
 * @update on 2017/10/26
 * description: 规则操作控制器
 *               业务规则用groupId+artifactId识别；版本用version识别。
 */
@Slf4j
@Controller
@RequestMapping("rule")
public class RuleManagementController {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private RuleLoadService ruleLoadService;

    @Autowired
    private RuleCallService ruleCallService;

    @Autowired
    private RuleInfoMapper ruleInfoMapper;

    /**
     * created by zhulili1, on 2017/11/2
     * description: groupId, artifactId验重
     **/
    @RequestMapping("checkDuplication")
    @ResponseBody
    public String checkDuplication(@RequestParam("groupId") String groupId,
                                         @RequestParam("artifactId") String artifactId) {
        String result;
        try {
            int count = ruleInfoMapper.countRecordByGroupArtifactId(groupId, artifactId);
            if (count > 0) {
                // 数据已存在
                result = "true";
            } else {
                // 数据不存在
                result = "false";
            }
        } catch(Exception e){
            log.error("验重异常: {}", e.getMessage(), e);
            result = "error";
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", result);
        return result;
    }

    /**
     * created by zhulili1, on 2017/10/20
     * @update on 2017/10/28
     * description: 创建规则
     **/
    @RequestMapping(value="creation")
    public String create(Model model){
        model.addAttribute("ruleCreateVo", new RuleCreateVo());
        return "/config/create1";

    }
    /**
     * created by zhulili1, on 2017/10/28
     * description: 创建规则
     **/
    @RequestMapping(value="create_new", method = RequestMethod.POST)
    public String createNewRule(HttpServletRequest request, RuleCreateVo ruleCreateVo,
                                @RequestParam("description") List<String> descriptionList, ModelMap modelMap) {
        // 日志前缀
        final String logPrefix = "创建规则, ";
        try {
            RuleCreateBo ruleCreateBo = new RuleCreateBo();
            BeanUtils.copyProperties(ruleCreateVo, ruleCreateBo);
            List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("file");
            List<String> drlList = new ArrayList<>();
            for (int i =0; i< files.size(); ++i) {
                // 读取文件内容
                MultipartFile file = files.get(i);
                byte[] bytes = file.getBytes();
                String content = new String(bytes, "UTF-8");
                drlList.add(content);
            }
            log.info("{}入参: {}, {}, {}", logPrefix, ruleCreateVo.toString(),
                                            drlList.toString(), descriptionList.toString());
            ruleService.createRule(ruleCreateBo, drlList, descriptionList);
            modelMap.addAttribute("message", "创建成功");
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            modelMap.addAttribute("message", "创建失败:" + e.getMessage());
        }
        return "/config/create1";
    }

    @RequestMapping(value="create_rule", method = RequestMethod.POST, params="method=saveRule")
    public String createRule(@RequestParam("file")MultipartFile file,
                             RuleCreateVo ruleCreateVo, ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "创建规则, ";
        log.info("{}入参：{}", logPrefix, ruleCreateVo.toString());
        try {
            RuleCreateBo ruleCreateBo = new RuleCreateBo();
            BeanUtils.copyProperties(ruleCreateVo, ruleCreateBo);
            byte[] bytes = file.getBytes();
            String content = new String(bytes, "UTF-8");
            ruleCreateBo.setDrl(content);
            ruleService.createRuleInfo(ruleCreateBo);
            modelMap.addAttribute("message", "创建成功");
        } catch (Exception e) {
            log.error("{}异常: {}", logPrefix, e.getMessage());
            modelMap.addAttribute("message", "创建失败: "+ e.getMessage());
        }
        return "/config/ruleCreation";
    }
    /**
     * created by zhulili1, on 2017/10/20
     * description: 创建规则,并加载
     **/
    @RequestMapping(value="create_rule", method = RequestMethod.POST, params="method=saveAndLoad")
    public String createRuleAndLoad(@RequestParam("file")MultipartFile file,
                                    @ModelAttribute RuleCreateVo ruleCreateVo,
                                    ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "创建规则并加载, ";
        log.info("{}入参：{}", logPrefix, ruleCreateVo.toString());
        RuleCreateBo ruleCreateBo = new RuleCreateBo();
        BeanUtils.copyProperties(ruleCreateVo, ruleCreateBo);
        try {
            byte[] bytes = file.getBytes();
            String content = new String(bytes, "UTF-8");
            ruleCreateBo.setDrl(content);
            ruleService.createAndLoadRule(ruleCreateBo);
            modelMap.addAttribute("message", "规则创建成功, 已加载");
        } catch (Exception e) {
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            modelMap.addAttribute("message", "规则创建失败或加载失败：" + e.getMessage());
        }
        return "/config/ruleCreation";
    }

    /**
     * created by zhulili1, on 2017/10/23
     * description: 查询规则
     **/
    @RequestMapping("query")
    public String query(Model model){
        model.addAttribute("ruleVersionList", null);
        return "/config/query";
    }
    @RequestMapping(value="query_rule", method = RequestMethod.POST)
    public String queryRule(@RequestParam(value="groupId", required=false) String groupId,
                            @RequestParam(value="artifactId", required=false) String artifactId,
                            ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "查询规则, ";
        log.info("{}入参: groupId={}, artifactId={}", logPrefix, groupId, artifactId);
        List<RuleVersionBo> ruleVersionBoList = ruleService.queryRuleVersionList(groupId, artifactId);
        log.info("{}返参{}", logPrefix, ruleVersionBoList.toString());
        modelMap.addAttribute("ruleVersionList", ruleVersionBoList);
        return "/config/query";
    }

    /**
     * created by zhulili1, on 2017/10/23
     * description: 查看规则文件列表
     **/
    @RequestMapping("queryDetail")
    public String queryDetail(@RequestParam("groupId") String groupId, @RequestParam("artifactId") String artifactId,
                              @RequestParam("version") String version, ModelMap modelMap, HttpSession httpSession){
        // 日志前缀
        final String logPrefix = "查看规则文件列表, ";
        log.info("{}入参: groupId={}, artifactId={}, version={}", logPrefix, groupId, artifactId, version);
        RuleDetailQueryVo ruleDetailQueryVo = new RuleDetailQueryVo();
        ruleDetailQueryVo.setGroupId(groupId);
        ruleDetailQueryVo.setArtifactId(artifactId);
        ruleDetailQueryVo.setVersion(version);
        ruleDetailQueryVo.setRuleInfoList(ruleService.queryRuleInfoList(groupId, artifactId, version));
        modelMap.addAttribute("ruleDetailQueryVo", ruleDetailQueryVo);
        httpSession.setAttribute("ruleDetailQueryVo", ruleDetailQueryVo);
        return "/config/queryDetail";
    }

    /**
     * created by zhulili1, on 2017/10/24
     * description: 启用规则
     *               启用某一版本的规则, 同时禁用同一规则的其他版本。
     **/
    @RequestMapping("load")
    public String load(@RequestParam("groupId") String groupId, @RequestParam("artifactId") String artifactId,
                              @RequestParam("version") String version, ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "启用规则, ";
        log.info("{}入参: groupId={}, artifactId={}, version={}", logPrefix, groupId, artifactId, version);
        try {
//            boolean result = ruleService.loadAndUnloadRule(groupId, artifactId, version);
//            if (result) {
//                modelMap.addAttribute("message", "加载成功");
//            } else {
//                modelMap.addAttribute("message", "加载失败: 未查询到规则记录");
//            }
        } catch(Exception e){
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            modelMap.addAttribute("message", "加载异常：" + e.getMessage());
        }
        List<RuleVersionBo> ruleVersionBoList = ruleService.queryRuleVersionList(groupId, artifactId);
        modelMap.addAttribute("ruleVersionList", ruleVersionBoList);
        return "/config/query";
    }

    /**
     * created by zhulili1, on 2017/10/27
     * description: 禁用规则(修改状态)
     **/
    @RequestMapping("unload")
    public String unload(@RequestParam("groupId") String groupId, @RequestParam("artifactId") String artifactId,
                         @RequestParam("version") String version, ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "禁用规则, ";
        log.info("{}入参: groupId={}, artifactId={}, version={}", logPrefix, groupId, artifactId, version);
        try {
            RuleVersionBo ruleVersionBo = new RuleVersionBo(groupId, artifactId, version);
            ruleService.unloadRule(ruleVersionBo);
            modelMap.addAttribute("message", "禁用规则成功");
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            modelMap.addAttribute("message", "禁用规则异常: " + e.getMessage());
        }
        List<RuleVersionBo> ruleVersionBoList = ruleService.queryRuleVersionList(groupId, artifactId);
        modelMap.addAttribute("ruleVersionList", ruleVersionBoList);
        return "/config/query";
    }
    /**
     * created by zhulili1, on 2017/10/29
     * description: 修改规则
     **/
    @RequestMapping("update")
    public String update(@RequestParam("groupId") String groupId, @RequestParam("artifactId") String artifactId,
                         @RequestParam("version") String version, ModelMap modelMap, HttpSession httpSession
                         ){
        // 日志前缀
        final String logPrefix = "修改规则, ";
        log.info("{}入参: groupId={}, artifactId={}, version={}", logPrefix, groupId, artifactId, version);
        try {
            List<RuleInfo> ruleInfoList = ruleService.queryRuleInfoList(groupId, artifactId, version);
            log.info("{}返回: {}", logPrefix, ruleInfoList);

            /*            */
            List<RuleUpdateVo> ruleUpdateVoList = Lists.transform(ruleInfoList, ruleInfo -> {
                RuleUpdateVo ruleUpdateVo = new RuleUpdateVo();
                BeanUtils.copyProperties(ruleInfo, ruleUpdateVo);
                ruleUpdateVo.setRadioName("pid" + ruleInfo.getPid());
                return ruleUpdateVo;
            });
            modelMap.addAttribute("ruleUpdateVoList", ruleUpdateVoList);

            modelMap.addAttribute("ruleInfoList", ruleInfoList);
            modelMap.addAttribute("groupId", groupId);
            modelMap.addAttribute("artifactId", artifactId);
            modelMap.addAttribute("version", version);

        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
        }
        return "/config/editDrl";
    }

    /**
     * created by zhulili1, on 2017/10/29
     * description: 修改规则
     **/
    @RequestMapping(value="edit_drl", method = RequestMethod.POST)
    public String updateDrl(@RequestParam("groupId") String groupId, @RequestParam("artifactId") String artifactId,
                            @RequestParam("version") String version,
                            @RequestParam("description") List<String> descriptionList,
                            @RequestParam("radioName") List<String> radioNameList,
                            @RequestParam("drl") List<String> drlList,
                            @RequestParam("drl") String drl,
                            HttpServletRequest request,
                            ModelMap modelMap
                            ){
//        List<RuleUpdateVo> ruleUpdateVoList = (List<RuleUpdateVo>)httpSession.getAttribute("ruleUpdateVoList");

        // 日志前缀
        final String logPrefix = "修改规则, ";
        log.info("{}入参: groupId={}, artifactId={}, version={}", logPrefix, groupId, artifactId, version);

        try {
            // 解析规则内容修改来源
            List<String> drlOriginList = new ArrayList<>();
            for (int i = 0; i < radioNameList.size(); i++) {
                String type = request.getParameter(radioNameList.get(i));
                drlOriginList.add(type);
            }
            // 处理原来仅有1条规则记录的情况
            if (drlOriginList.size() == 1) {
                drlList.clear();
                drlList.add(drl);
            }
            List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("file");
            RuleVersionBo ruleVersionBo = new RuleVersionBo(groupId, artifactId, version);
//            version = ruleService.updateDrl(ruleVersionBo, descriptionList, drlOriginList, drlList, files);

            // 查询新的list, 到查询页面, 返回新版本信息到message
            List<RuleVersionBo> ruleVersionBoList = ruleService.queryRuleVersionList(groupId, artifactId);
            modelMap.addAttribute("ruleVersionList", ruleVersionBoList);
            modelMap.addAttribute("message","修改成功, 新版本为：[groupId:" + groupId +
                    ", artifactId:" + artifactId + "version:" + version + "]");

        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            modelMap.addAttribute("修改失败：" + e.getMessage());
        }
        return "/config/query";
    }

    /**
     * created by zhulili1, on 2017/10/31
     * description: 调用规则
     **/
    @RequestMapping("call")
    public String call(ModelMap modelMap) {
        modelMap.addAttribute("result", null);
        return "config/call";
    }
    @RequestMapping(value="call_rule", method = RequestMethod.POST)
    public String callRule(@RequestParam("groupId") String groupId,
                           @RequestParam("artifactId") String artifactId,
                           @RequestParam("version") String version,
                           @RequestParam(value = "type", required = false) List<String> typeList,
                           @RequestParam(value = "key", required = false) List<String> keyList,
                           @RequestParam(value = "value", required = false) List<String> valueList,
                           ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "调用规则, ";
        try {
            log.info("{}入参: groupId={}, artifactId={}, version={}", logPrefix, groupId, artifactId, version);
            RuleVersionBo ruleVersionBo = new RuleVersionBo(groupId, artifactId, version);
            Map<String, Object> dataMap = null;
            if (typeList != null && keyList != null && valueList != null) {
                // 组装数据
                dataMap = ruleService.createDataMap(typeList, keyList, valueList);
            }
            // 调用规则
            ruleCallService.testCallRule(ruleVersionBo, dataMap);
            modelMap.addAttribute("result", dataMap);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            modelMap.addAttribute("result", "调用规则异常：" + e.getMessage());
        }
        return "/config/call";
    }


    /**
     * created by zhulili1, on 2017/10/24
     * description: 修改版本信息
     **/
    @RequestMapping("upgrade")
    public String upgrade(@RequestParam("groupId") String groupId, @RequestParam("artifactId") String artifactId,
                          @RequestParam("version") String version, ModelMap modelMap, HttpSession httpSession){
        // 日志前缀
        final String logPrefix = "修改版本信息, ";
        log.info("{}入参: groupId={}, artifactId={}, version={}", logPrefix, groupId, artifactId, version);
        RuleVersionBo oldVersion = new RuleVersionBo(groupId, artifactId, version);
        RuleVersionBo newVersion = new RuleVersionBo(groupId, artifactId, version);
        modelMap.addAttribute("newVersion", newVersion);
        httpSession.setAttribute("oldVersion", oldVersion);
        return "/config/upgrade";
    }
    /**
     * created by zhulili1, on 2017/10/24
     * description: 修改版本信息
     **/
    @RequestMapping("upgrade_rule")
    public String upgradeVersion(@ModelAttribute("newVersion") RuleVersionBo newVersion,
                                 ModelMap modelMap, HttpSession httpSession){
        // 日志前缀
        final String logPrefix = "修改版本信息, ";
        RuleVersionBo oldVersion = (RuleVersionBo) httpSession.getAttribute("oldVersion");
        log.info("{}当前版本（旧版本）{}", logPrefix, oldVersion.toString());
        log.info("{}新版本: {}", logPrefix, newVersion);
        try {
            ruleService.upgradeRuleVersion(oldVersion, newVersion);
            List<RuleVersionBo> ruleVersionBoList = ruleService.queryRuleVersionList(newVersion.getGroupId(),
                    newVersion.getArtifactId());
            modelMap.addAttribute("ruleVersionList", ruleVersionBoList);
            modelMap.addAttribute("message","版本信息修改成功");
        } catch(Exception e){
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            modelMap.addAttribute("message","版本信息修改失败：" + e.getMessage());
        }

        return "/config/query";
    }
    /**
     * created by zhulili1, on 2017/10/23
     * description: 删除一个版本的规则
     **/
    @RequestMapping("delGroup")
    public String delGroup(@RequestParam("groupId") String groupId, @RequestParam("artifactId") String artifactId,
                              @RequestParam("version") String version, ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "删除一个版本的规则, ";
        log.info("{}入参: groupId={}, artifactId={}, version={}", logPrefix, groupId, artifactId, version);
        RuleVersionBo ruleVersionBo = new RuleVersionBo(groupId, artifactId, version);
        try {
            ruleService.delRuleByVersion(ruleVersionBo);
            List<RuleVersionBo> ruleVersionBoList = ruleService.queryRuleVersionList(groupId, artifactId);
            modelMap.addAttribute("ruleVersionList", ruleVersionBoList);
            modelMap.addAttribute("message", "删除成功");
        } catch(Exception e){
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            modelMap.addAttribute("message", "删除失败"+e.getMessage());
        }
        return "/config/query";
    }
    /**
     * created by zhulili1, on 2017/10/23
     * description: 编辑规则文件
     **/
    @RequestMapping("edit")
    public String queryDetail(@RequestParam("pid") String pid, ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "编辑规则文件, ";
        log.info("{}入参: pid={}", logPrefix, pid);
        RuleInfo ruleInfo = ruleService.queryRuleInfo(pid);
        modelMap.addAttribute("ruleInfo", ruleInfo);
        return "/config/drl";
    }


    /**
     * created by zhulili1, on 2017/10/23
     * description: 删除规则文件
     **/
    @RequestMapping("delDrl")
    public String delDrl(@RequestParam("pid") String pid, ModelMap modelMap, HttpSession httpSession){
        // 日志前缀
        final String logPrefix = "删除规则文件, ";
        log.info("{}入参: pid={}", logPrefix, pid);
        RuleDetailQueryVo ruleDetailQueryVo = (RuleDetailQueryVo)httpSession.getAttribute("ruleDetailQueryVo");
        try {
            ruleService.delDrl(pid);
            ruleDetailQueryVo.setRuleInfoList(ruleService.queryRuleInfoList(ruleDetailQueryVo.getGroupId(),
                            ruleDetailQueryVo.getArtifactId(), ruleDetailQueryVo.getVersion()));
        } catch(Exception e){
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            modelMap.addAttribute("message", "删除失败"+e.getMessage());
        }
        modelMap.addAttribute("message", "删除成功");
        modelMap.addAttribute("ruleDetailQueryVo", ruleDetailQueryVo);
        return "/config/queryDetail";
    }
    /**
     * created by zhulili1, on 2017/10/24
     * description: 修改drl信息
     **/
    @RequestMapping(value="update", method = RequestMethod.POST, params="method=updateDrl")
    public String update(@RequestParam("file")MultipartFile file, @ModelAttribute RuleInfo ruleInfo,
                             ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "修改drl, ";

        log.info("{}入参：{}", logPrefix, ruleInfo.toString());
        try {
            byte[] bytes = file.getBytes();
            String content = new String(bytes, "UTF-8");
            if (!StringUtils.isEmpty(content)) {
                ruleInfo.setDrl(content);
            }
            ruleService.updateDrl(ruleInfo);
            modelMap.addAttribute("message", "修改成功");
        } catch (Exception e) {
            modelMap.addAttribute("message", "修改失败"+e.getMessage());
        }

        return "/config/drl";
    }

    /**
     * created by zhulili1, on 2017/10/24
     * description: 修改drl并加载
     **/
    @RequestMapping(value="update", method = RequestMethod.POST, params="method=updateAndLoad")
    public String updateAndLoad(@RequestParam("file")MultipartFile file, @ModelAttribute RuleInfo ruleInfo,
                         ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "修改drl并加载, ";

        log.info("{}入参：{}", logPrefix, ruleInfo.toString());
        try {
            byte[] bytes = file.getBytes();
            String content = new String(bytes, "UTF-8");
            if (!StringUtils.isEmpty(content)) {
                ruleInfo.setDrl(content);
            }
            ruleService.updateDrlAndLoadRule(ruleInfo);
            modelMap.addAttribute("message", "修改并加载成功");
        } catch (Exception e) {
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            modelMap.addAttribute("message", "修改或加载失败"+e.getMessage());
        }

        return "/config/drl";
    }

    /**
     * created by zhulili1, on 2017/10/16
     * description: 重新加载规则
     **/
    @RequestMapping("reload/{groupId}&{artifactId}&{version}.html")
    public String reloadRules(@PathVariable("groupId") String groupId, @PathVariable("artifactId") String artifactId,
                              @PathVariable("version") String version){
        // 日志前缀
        final String logPrefix = "重新加载规则, ";

        try{
            log.info("{}传入参数groupId={}, artifact={}, version={}", logPrefix, groupId, artifactId, version);
            RuleVersionBo ruleVersionBo = new RuleVersionBo(groupId, artifactId, version);
            ruleService.reloadRule(ruleVersionBo);
        } catch(Exception e){
            log.error("{}异常,{}", logPrefix, e.getMessage(), e);
            return "error: " + e.getMessage();
        }
        return "success";
    }


}
