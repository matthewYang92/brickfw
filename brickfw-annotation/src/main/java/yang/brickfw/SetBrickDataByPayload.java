package yang.brickfw;

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
public @interface SetBrickDataByPayload {
    /**
     * 组件类型
     *
     * @return
     */
    String value();
}
