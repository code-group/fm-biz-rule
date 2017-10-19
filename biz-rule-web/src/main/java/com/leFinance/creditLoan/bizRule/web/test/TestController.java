package com.leFinance.creditLoan.bizRule.web.test;

import com.leFinance.creditLoan.bizRule.bo.RuleCallBo;
import com.leFinance.creditLoan.bizRule.dto.RuleReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leFinance.creditLoan.bizRule.service.RuleCallService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;


/**
 * @author: zhulili1
 * @date: 2017/10/10
 * @description:
 */
@RequestMapping("test")
@Slf4j
@Controller
public class TestController {

    @Autowired
    private RuleCallService ruleCallService;

    @RequestMapping("/drools")
    public String testDrools(Model model) {
        RuleCallBo ruleCallBo = new RuleCallBo();
        ruleCallBo.setGroupId("fm.leFinance.creditLoan");
        ruleCallBo.setArtifactId("contract");
        ruleCallBo.setVersion("1.0.0");
        ruleCallBo.setContainerName("contractContainer");
        ruleCallBo.setKsessionName("contract_ksession");
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
     * 多文件具体上传时间，主要是使用了MultipartHttpServletRequest和MultipartFile
     * @param request
     * @return
     */
    @RequestMapping(value="/batch/upload", method=RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(HttpServletRequest request, @RequestParam("username") String username){
        System.out.println("传参：" + username);
        List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i =0; i< files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
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
}
