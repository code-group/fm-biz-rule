import com.leFinance.creditLoan.bizRule.bo.RuleVersionBo;
import com.leFinance.creditLoan.bizRule.common.utils.KieUtil;
import com.leFinance.creditLoan.bizRule.service.utils.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private RuleService ruleService;

    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 测试加载规则
     **/
    @Test
    public void testLoad(){
        KieSession kieSession = KieUtil.getKieSession("test", "test",
                "1.0.0", "testKsession");
        kieSession.fireAllRules();

        Map<String, Object> map = new HashMap<>();
        kieSession = KieUtil.getKieSession("fm.leFinance.creditLoan", "contract",
                "1.0.1", "contract_ksession");
        kieSession.insert(map);
        kieSession.fireAllRules();
        System.out.println(map.get("createDBContract"));
    }

    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 测试重载规则
     **/
    @Test
    public void testReload(){
        testLoad();

        log.info("重新加载规则");
        try{
            RuleVersionBo ruleVersionBo = new RuleVersionBo("fm.leFinance.creditLoan", "contract", "1.0.1");
            ruleService.reloadRule(ruleVersionBo);

        } catch(Exception e){
            log.error("{}", e.getMessage(), e);
        }

        testLoad();

    }
    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 测试升级版本号
     **/
    @Test
    public void testUpgradeRule(){
        RuleVersionBo oldVersion = new RuleVersionBo("fm.leFinance.creditLoan", "contract", "1.0.0");
        RuleVersionBo newVersion = new RuleVersionBo("fm.leFinance.creditLoan", "contract", "1.1.0");
        try{
            ruleService.upgradeRuleVersion(oldVersion, newVersion);
        } catch(Exception e){
            log.error("{}", e.getMessage(), e);
        }
    }

}
