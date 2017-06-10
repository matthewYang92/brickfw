package yang.brickfw;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Brick事件访问注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnBrickEvent {
    /**
     * 组件类型
     * @return
     */
    String value();

    /**
     * 事件类型
     * @return
     */
    int eventType();
}
