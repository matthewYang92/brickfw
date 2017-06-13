package yang.brickviewdemo.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import yang.brickfw.AbstractBrickHolder;
import yang.brickfw.BrickHolder;
import yang.brickfw.BrickInfo;
import yang.brickfw.BrickView;
import yang.brickviewdemo.BrickType;

@BrickHolder(BrickType.TEXT)
public class SimpleTextHolder extends AbstractBrickHolder {

    @BrickView(BrickType.TEXT)
    private TextView textView;

    public SimpleTextHolder(@NonNull View view) {
        super(view);
        textView = (TextView) view;
        setTextViewLayoutParams();
    }

    private void setTextViewLayoutParams() {
        WindowManager wm = (WindowManager) textView.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        textView.setLayoutParams(new ViewGroup.LayoutParams(wm.getDefaultDisplay().getWidth(), 200));
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
    }

    @Override
    public void setBrickInfo(BrickInfo info) {
        String content = (String) info.getExtra();
        textView.setText(content);
    }
}