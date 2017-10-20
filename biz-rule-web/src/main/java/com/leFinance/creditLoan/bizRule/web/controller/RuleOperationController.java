package com.leFinance.creditLoan.bizRule.web.controller;

import com.leFinance.creditLoan.bizRule.bo.RuleCreateBo;
import com.leFinance.creditLoan.bizRule.bo.RuleVersionBo;
import com.leFinance.creditLoan.bizRule.common.utils.StringUtil;
import com.leFinance.creditLoan.bizRule.domain.RuleInfo;
import com.leFinance.creditLoan.bizRule.service.utils.RuleService;
import com.leFinance.creditLoan.bizRule.vo.RuleCreateVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author: zhulili1
 * @date: 2017/10/17
 * @description: 规则操作控制器
 */
@Slf4j
@Controller
@RequestMapping("rule")
public class RuleOperationController {

    @Autowired
    private RuleService reloadService;

    @Autowired
    private RuleService ruleService;


    /**
     * created by zhulili1, on 2017/10/20
     * @Description: 创建规则
     **/
    @RequestMapping("creation")
    public String create(Model model){
        model.addAttribute("ruleCreateVo", new RuleCreateVo());
        return "/ruleCreation";
    }
    @RequestMapping("create_rule")
    public String createRule(@RequestParam("file")MultipartFile file, @ModelAttribute RuleCreateVo ruleCreateVo){
        // 中文需要转码
        StringUtil.transformCode(ruleCreateVo);

        log.info(ruleCreateVo.toString());
        RuleCreateBo ruleCreateBo = new RuleCreateBo();
        BeanUtils.copyProperties(ruleCreateVo, ruleCreateBo);
        try {
            byte[] bytes = file.getBytes();
            String content = new String(bytes, "UTF-8");
            ruleCreateBo.setDrl(content);
            ruleService.createRuleInfo(ruleCreateBo);
        } catch (Exception e) {
            return "You failed to upload " + " =>" + e.getMessage();
        }
        return "/ruleCreation";
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
            reloadService.reloadRule(ruleVersionBo);
        } catch(Exception e){
            log.error("{}异常,{}", logPrefix, e.getMessage(), e);
            return "error: " + e.getMessage();
        }
        return "success";
    }


    @RequestMapping(value="/create", params = "method=postConfig", method = RequestMethod.POST)
    public String postConfig(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam("groupId") String groupId, @RequestParam("artifactId") String artifactId,
                             @RequestParam("version") String version, ModelMap modelMap) {
        response.setCharacterEncoding("GBK");

        boolean checkSuccess = true;
        String errorMessage = "参数不完整";
//        if(StringUtils.isBlank(groupId) || StringUtils.isBlank(artifactId) || StringUtils.isBlank(version)) {
//            checkSuccess = false;
//        }
        if (!checkSuccess) {
            modelMap.addAttribute("message", errorMessage);
            return "/admin/config/new";
        }

//        dataId = dataId.trim();
//        group = group.trim();
//
//        this.configService.addConfigInfo(dataId, group, content);

        modelMap.addAttribute("message", "提交成功!");
        return modelMap.get("message").toString();
    }
}
