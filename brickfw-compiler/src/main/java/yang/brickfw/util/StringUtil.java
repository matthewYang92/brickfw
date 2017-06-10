package yang.brickfw.util;

/**
 * author: Matthew Yang on 17/6/9
 * e-mail: yangtian@yy.com
 */

public class StringUtil {

    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
