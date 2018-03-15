import com.virgo.finance.loan.common.data.Message;
import com.virgo.finance.loan.rule.api.RuleConfigInterface;
import com.virgo.finance.loan.rule.api.dto.*;
import com.virgo.finance.loan.rule.web.Application;
import com.virgo.finance.loan.rule.web.interfaces.impl.RuleInterfaceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhulili1
 * date: 2017/10/11
 * description:
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class RuleRequestTest {

    @Autowired
    private RuleInterfaceImpl ruleInterfaceImpl;

    @Autowired
    private RuleConfigInterface ruleConfigInterface;

    @Test
    public void testImpl(){
        RuleReqDto ruleReqDto = new RuleReqDto();
        ruleReqDto.setGroupId("loan.leFinance.creditLoan");
        ruleReqDto.setArtifactId("contract");
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("feeValue", new BigDecimal(1));
        dataMap.put("something", "haha");
        ruleReqDto.setDataMap(dataMap);

        try{
//            Message<Map<String, Object>> message = ruleInterfaceImpl.callRule(ruleReqDto);
            Message<RuleResDto> message = ruleInterfaceImpl.callRule(ruleReqDto);
            log.info("{}", message.getData());

        } catch(Exception e){
            log.error("{}", e.getMessage(), e);

        }
    }

    @Test
    public void testLoad() {
        try {
            String id = "test_load";
            RuleReqDto ruleReqDto = new RuleReqDto();
            ruleReqDto.setGroupId(id);
            ruleReqDto.setArtifactId(id);
            ruleReqDto.setDataMap(null);
            Message<RuleResDto> message = ruleInterfaceImpl.callRule(ruleReqDto);
            log.info("第一次调用结果{}", message.getData());

            ruleReqDto.setDataMap(new HashMap<>());
            message = ruleInterfaceImpl.callRule(ruleReqDto);
            log.info("第二次调用结果{}", message.getData());

            // 启用1
            RuleLoadReqDto ruleLoadReqDto = new RuleLoadReqDto();
            ruleLoadReqDto.setGroupId(id);
            ruleLoadReqDto.setArtifactId(id);
            ruleLoadReqDto.setVersion("2");
            Message<Boolean> result = ruleConfigInterface.loadRule(ruleLoadReqDto);
            log.info("启用结果, {}", result.toString());

            message = ruleInterfaceImpl.callRule(ruleReqDto);
            log.info("第三次调用结果{}", message.getData());

        } catch(Exception e){
            log.error("{}", e.getMessage(), e);
        }
    }

    /**
     * <P>date:        2018/1/4</P>
     * <P>description: 测试分页查询</P>
     **/
    @Test
    public void test() {
        RuleInfoQueryReqDto ruleInfoQueryReqDto = new RuleInfoQueryReqDto();
        ruleInfoQueryReqDto.setGroupId("loan");
        ruleInfoQueryReqDto.setArtifactId("loan");
        ruleInfoQueryReqDto.setCurrentPageNo(1);
        ruleInfoQueryReqDto.setPageSize(10);
        Message<RuleInfoQueryResPageDto> message = ruleConfigInterface.getRuleInfoList(ruleInfoQueryReqDto);
        log.info(message.toString());
    }

}
