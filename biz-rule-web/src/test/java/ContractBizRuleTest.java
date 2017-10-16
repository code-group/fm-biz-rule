import com.leFinance.creditLoan.bizRule.interfaces.impl.ContractBizRuleInterfaceImpl;
import com.leFinance.creditLoan.bizRule.web.config.drools.DroolsConfig;
import data.Message;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhulili1
 * @date: 2017/10/11
 * @description:
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = com.leFinance.creditLoan.bizRule.web.Application.class)
public class ContractBizRuleTest {

    @Autowired
    private ContractBizRuleInterfaceImpl contractBizRuleInterfaceImpl;

    @Autowired
    @Qualifier("droolsConfig")
    private DroolsConfig droolsConfig;

    @Test
    public void testContractBizRule(){
//        CreateContractReqDto createContractReqDto = new CreateContractReqDto();
//        createContractReqDto.setFeeValue(new BigDecimal(1));
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("feeValue", new BigDecimal(1));
        dataMap.put("something", "haha");

        try{
            Message<Boolean> message = contractBizRuleInterfaceImpl.createDBContract(dataMap);
            log.info("{}", message.getData());

        } catch(Exception e){
            log.error("{}", e.getMessage(), e);
        }
    }

    @Test
    public void testReset(){
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.write( "src/main/resources/rules/test.drl", "package test;\n " +
                        "import com.leFinance.creditLoan.bizRule.fact.ContractFact;\n" +
                        "dialect  \"mvel\" \n" +
                        "rule \"create guarantee contract\"\n " +
                        "lock-on-active\n " +
                        "when\n " +
                        "   $b : ContractFact(feeValue != null)\n " +
                        "then\n " +
                        "   System.out.println(\"test\");\n " +
                        "end");

        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }

//        droolsAutoConfiguration.resetKieSession(contractSession, "contractKieSession");

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("feeValue", new BigDecimal(1));
        dataMap.put("something", "haha");

        try{
            Message<Boolean> message = contractBizRuleInterfaceImpl.createDBContract(dataMap);
            log.info("{}", message.getData());

        } catch(Exception e){
            log.error("{}", e.getMessage(), e);
        }

    }
}
