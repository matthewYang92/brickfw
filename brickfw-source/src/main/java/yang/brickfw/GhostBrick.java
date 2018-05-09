package yang.brickfw;

import android.view.View;
import android.view.ViewGroup;

/**
 * 用来显示不支持的组件内容
 */
public class GhostBrick extends AbstractBrickHolder {

    public GhostBrick(final ViewGroup parent) {
        super(new View(parent.getContext()));
    }

    @Override
    public void setBrickInfo(BrickInfo info) {
    }

    @Override
    public void onViewRecycled() {

    }

}
