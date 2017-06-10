package yang.brickfw;

import android.content.Context;

/**
 * 组件工人
 * 组件工厂注册组件时会为每个类型招募一个组件工人
 *
 * 必须通过@Brick注解声明，否则无法正确注册组件
 *
 */
public abstract class AbstractBrickBuilder {

    public abstract AbstractBrickHolder create(Context context);
}
