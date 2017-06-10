package yang.brickfw;

import android.view.View;

/**
 * 事件处理类
 */
public abstract class AbstractBrickEventBinder {

    public abstract void bindBrickOnItemClick(Object handler, View itemView, BrickInfo info);
    public abstract void bindBrickOnItemLongClick(Object handler, View itemView, BrickInfo info);
    public abstract void bindBrickOnEvent(Object handler, View itemView, BrickInfo info);

}
