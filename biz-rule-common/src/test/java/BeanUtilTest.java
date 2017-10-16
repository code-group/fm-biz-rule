import com.leFinance.creditLoan.bizRule.common.utils.BeanUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhulili1
 * @date: 2017/10/12
 * @description: BeanUtil测试类
 */
public class BeanUtilTest {

    public static void main(String[] args) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("feeValue", new BigDecimal(1));
        dataMap.put("something", "haha");

        TestFact testFact = (TestFact)BeanUtil.setBeanAttributes(TestFact.class, dataMap);

        System.out.println(testFact.toString());

    }

}
