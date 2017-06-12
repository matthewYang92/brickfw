package yang.brickviewdemo.bricks;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import yang.brickfw.AbstractBrickHolder;
import yang.brickfw.BrickHolder;
import yang.brickfw.BrickInfo;
import yang.brickfw.BrickView;

@BrickHolder("simple_text")
public class SimpleTextHolder extends AbstractBrickHolder {

    @BrickView("simple_text")
    private TextView textView;

    public SimpleTextHolder(@NonNull View view) {
        super(view);
        textView = (TextView) view;
    }

    @Override public void setBrickInfo(BrickInfo info) {
        String content = (String) info.getExtra();
        textView.setText(content);
    }
}