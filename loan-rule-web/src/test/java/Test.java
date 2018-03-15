import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: zhull
 * date: 2017/11/16
 * description:
 */
public class Test {
    public static void main(String[] args) {
        HashMap source = new HashMap();
        source.put("key1","value1");
        source.put("key2","value2");

        for(Iterator keyItr = source.keySet().iterator(); keyItr.hasNext();) {
            Object key = keyItr.next();
            System.out.println(key + " : "+source.get(key));
        }

        Map targetMap = (HashMap)source.clone();
        targetMap.put("key1","target");
        targetMap.put("target","target");

        System.out.println("----------------- source ----------------");
        for(Iterator keyItr = source.keySet().iterator();keyItr.hasNext();){
            Object key = keyItr.next();
            System.out.println(key + " : "+source.get(key));
        }

        System.out.println("----------------- target ----------------");
        for(Iterator keyItr = targetMap.keySet().iterator();keyItr.hasNext();){
            Object key = keyItr.next();
            System.out.println(key + " : "+targetMap.get(key));
        }
    }
}
