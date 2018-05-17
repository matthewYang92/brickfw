package yang.brickfw;

/**
 * author: Matthew Yang on 18/5/9
 * e-mail: yangtian@yy.com
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BrickView设置数据setData方法注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface OnRecycled {
    /**
     * 组件类型
     *
     * @return
     */
    String value();
}
