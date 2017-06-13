package yang.brickviewdemo.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import yang.brickfw.AbstractBrickHolder;
import yang.brickfw.BrickHolder;
import yang.brickfw.BrickInfo;
import yang.brickfw.BrickView;

@BrickHolder("simple_image")
public class SimpleImageHolder extends AbstractBrickHolder {

    @BrickView("simple_image")
    private ImageView imageView;

    public SimpleImageHolder(@NonNull View view) {
        super(view);
        imageView = (ImageView) view;
    }

    @Override
    public void setBrickInfo(BrickInfo info) {
        String url = (String) info.getExtra();
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
}