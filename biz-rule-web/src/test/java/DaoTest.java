import com.leFinance.creditLoan.bizRule.dao.RuleInfoMapper;
import com.leFinance.creditLoan.bizRule.domain.RuleInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author: zhulili1
 * @date: 2017/10/12
 * @description: Dao测试类
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = com.leFinance.creditLoan.bizRule.web.Application.class)
public class DaoTest {

    @Resource
    private RuleInfoMapper ruleInfoMapper;

    @Test
    public void testDao(){
        RuleInfo ruleInfo = ruleInfoMapper.selectByPrimaryKey(1);
        System.out.println(ruleInfo);

    }
}
