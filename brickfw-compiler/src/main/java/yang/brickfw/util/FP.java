package yang.brickfw.util;

import java.util.Collection;
import java.util.Map;

/**
 * author: Matthew Yang on 17/6/8
 * e-mail: yangtian@yy.com
 */

public class FP {
    public static boolean empty(String str) {
        return null == str || str.isEmpty();
    }

    public static boolean empty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean empty(Map<?,?> map) {
        return map == null || map.isEmpty();
    }
}
