import com.leFinance.creditLoan.bizRule.common.utils.KieUtil;
import com.leFinance.creditLoan.bizRule.fact.ContractFact;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author: zhulili1
 * @date: 2017/10/16
 * @description:
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = com.leFinance.creditLoan.bizRule.web.Application.class)
public class BizRuleTest {

    @Value("${test.kie.container.name}")
    private String testContainer;
    @Value("${test.ksession.name}")
    private String testKsession;

    @Value("${contract.kie.container.name}")
    private String contractContainer;
    @Value("${contract.ksession.name}")
    private String contractKsession;

    @Test
    public void testLoad(){
        KieSession kieSession = KieUtil.getKieSession(testContainer, testKsession);
        kieSession.fireAllRules();

        ContractFact contractFact = new ContractFact();
        kieSession = KieUtil.getKieSession(contractContainer, contractKsession);
        kieSession.insert(contractFact);
        kieSession.fireAllRules();
        System.out.println(contractFact.getCreateDBContract());
    }
}
