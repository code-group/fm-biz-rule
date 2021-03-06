package com.virgo.finance.loan.rule.web.test;

import com.virgo.finance.loan.rule.common.utils.StringUtil;
import com.virgo.finance.loan.rule.dao.bo.RuleVersionBo;
import com.virgo.finance.loan.rule.service.RuleCallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: zhulili1
 * date: 2017/10/10
 * description:
 */
@RequestMapping("test")
@Slf4j
@Controller
public class TestController {

    @Autowired
    private RuleCallService ruleCallService;

    @RequestMapping("/drools")
    public String testDrools(Model model) {
        RuleVersionBo ruleCallBo = new RuleVersionBo();
        ruleCallBo.setGroupId("loan.leFinance.creditLoan");
        ruleCallBo.setArtifactId("contract");
        ruleCallBo.setVersion("1.0.0");
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("feeValue", new BigDecimal(1));
        dataMap.put("something", "haha");
        try {
            ruleCallService.callRule(ruleCallBo, dataMap);
            log.info("!!!{}", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("result", "success");
        return "common/result";
    }


    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello(Model model) {
        model.addAttribute("name", "zhull");
        return "hello";
    }

    @RequestMapping("/file")
    public String file(){
        return "/fileUpload";
    }
    /**
     * 多文件上传, 主要是使用了MultipartHttpServletRequest和MultipartFile
     * @param request
     * @return
     */
    @RequestMapping(value="/batch/upload", method=RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(HttpServletRequest request, @RequestParam("username") String username){

        try {
            System.out.println(username);
            username = StringUtil.transformCode(username);
            System.out.println("传参：" + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i =0; i< files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    String content = new String(bytes, "UTF-8");
                    System.out.println(content);
                } catch (Exception e) {
                    stream =  null;
                    return "You failed to upload " + i + " =>" + e.getMessage();
                }
            } else {
                return "You failed to upload " + i + " because the file was empty.";
            }
        }
        return "upload successful";

    }

    @RequestMapping("/chinese/{username}")
    public String link(@PathVariable("username") String username){
        System.out.println(username);
        return username;
    }
}
