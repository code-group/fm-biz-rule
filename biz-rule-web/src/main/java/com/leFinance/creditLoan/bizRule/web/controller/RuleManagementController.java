package com.leFinance.creditLoan.bizRule.web.controller;

import com.leFinance.creditLoan.bizRule.bo.RuleCreateBo;
import com.leFinance.creditLoan.bizRule.bo.RuleVersionBo;
import com.leFinance.creditLoan.bizRule.common.utils.StringUtil;
import com.leFinance.creditLoan.bizRule.domain.RuleInfo;
import com.leFinance.creditLoan.bizRule.service.utils.RuleLoadService;
import com.leFinance.creditLoan.bizRule.service.utils.RuleService;
import com.leFinance.creditLoan.bizRule.vo.RuleCreateVo;
import com.leFinance.creditLoan.bizRule.vo.RuleDetailQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * @author: zhulili1
 * @date: 2017/10/17
 * @description: 规则操作控制器
 */
@Slf4j
@Controller
@RequestMapping("rule")
public class RuleManagementController {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private RuleLoadService ruleLoadService;

    /**
     * created by zhulili1, on 2017/10/20
     * @Description: 创建规则
     **/
    @RequestMapping(value="creation")
    public String create(Model model){
        model.addAttribute("ruleCreateVo", new RuleCreateVo());
        return "/config/ruleCreation";
    }
    @RequestMapping(value="create_rule", method = RequestMethod.POST, params="method=saveRule")
    public String createRule(@RequestParam("file")MultipartFile file,
                             @ModelAttribute RuleCreateVo ruleCreateVo, ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "创建规则, ";
        // 中文需要转码
        StringUtil.transformCode(ruleCreateVo);
        log.info("{}入参：{}", logPrefix, ruleCreateVo.toString());
        RuleCreateBo ruleCreateBo = new RuleCreateBo();
        BeanUtils.copyProperties(ruleCreateVo, ruleCreateBo);
        try {
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
     * @Description: 创建规则,并加载
     **/
    @RequestMapping(value="create_rule", method = RequestMethod.POST, params="method=saveAndLoad")
    public String createRuleAndLoad(@RequestParam("file")MultipartFile file,
                                    @ModelAttribute RuleCreateVo ruleCreateVo,
                                    ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "创建规则并加载, ";
        // 中文需要转码
        StringUtil.transformCode(ruleCreateVo);
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
            log.error("{}", logPrefix);
            modelMap.addAttribute("message", "规则创建失败或加载失败：" + e.getMessage());
        }
        return "/config/ruleCreation";
    }

    /**
     * created by zhulili1, on 2017/10/23
     * @Description: 查询规则
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
     * @Description: 查看规则文件列表
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
     * @Description: 加载规则
     **/
    @RequestMapping("load")
    public String load(@RequestParam("groupId") String groupId, @RequestParam("artifactId") String artifactId,
                              @RequestParam("version") String version, ModelMap modelMap, HttpSession httpSession){
        // 日志前缀
        final String logPrefix = "加载规则, ";
        log.info("{}入参: groupId={}, artifactId={}, version={}", logPrefix, groupId, artifactId, version);
        try {
            ruleLoadService.loadRule(groupId, artifactId, version);
            modelMap.addAttribute("message", "加载成功");
        } catch(Exception e){
            log.error("{}异常：{}", logPrefix, e.getMessage(), e);
            modelMap.addAttribute("message", "加载失败：" + e.getMessage());
        }
        List<RuleVersionBo> ruleVersionBoList = ruleService.queryRuleVersionList(groupId, artifactId);
        modelMap.addAttribute("ruleVersionList", ruleVersionBoList);
        return "/config/query";
    }
    /**
     * created by zhulili1, on 2017/10/24
     * @Description: 修改版本信息
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
     * @Description: 修改版本信息
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
     * @Description: 删除一个版本的规则
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
     * @Description: 编辑规则文件
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
     * @Description: 删除规则文件
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
     * @Description: 修改drl信息
     **/
    @RequestMapping(value="update", method = RequestMethod.POST, params="method=updateDrl")
    public String update(@RequestParam("file")MultipartFile file, @ModelAttribute RuleInfo ruleInfo,
                             ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "修改drl, ";
        // 中文需要转码
        StringUtil.transformCode(ruleInfo);

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
     * @Description: 修改drl并加载
     **/
    @RequestMapping(value="update", method = RequestMethod.POST, params="method=updateAndLoad")
    public String updateAndLoad(@RequestParam("file")MultipartFile file, @ModelAttribute RuleInfo ruleInfo,
                         ModelMap modelMap){
        // 日志前缀
        final String logPrefix = "修改drl并加载, ";
        // 中文需要转码
        StringUtil.transformCode(ruleInfo);

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
     * @Description: 重新加载规则
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
