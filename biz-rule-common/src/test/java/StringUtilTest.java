import com.leFinance.creditLoan.bizRule.common.utils.StringUtil;

/**
 * @author: zhulili1
 * @date: 2017/10/20
 * @description:
 */
public class StringUtilTest {
    public static void main(String[] args) {
        TestFact o = new TestFact();
        o.setAnything("中文");
        o.setSomething("org.xxx");
        StringUtil.transformCode(o);
    }
}
