package com.leFinance.creditLoan.bizRule.web.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.leFinance.creditLoan.bizRule.service.ContractBizRuleService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhulili1
 * @date: 2017/10/10
 * @description:
 */
@RequestMapping("test")
@Slf4j
@RestController
public class TestController {

    @Autowired
    private ContractBizRuleService contractBizRuleService;

    @RequestMapping("/drools")
    public String testDrools() {
//        CreateContractReqDto createContractReqDto = new CreateContractReqDto();
//        createContractReqDto.setFeeValue(new BigDecimal(1));
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("feeValue", new BigDecimal(1));
        dataMap.put("something", "haha");
        try {
            boolean result = contractBizRuleService.createDBContract(dataMap);
            log.info("!!!{}", result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";
    }

}
