package yang.brickviewdemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import yang.brickfw.BrickEventHandler;
import yang.brickfw.BrickView;
import yang.brickfw.IBrickEventHandler;
import yang.brickfw.SetBrickData;
import yang.brickviewdemo.R;
import yang.brickviewdemo.entity.ImageText;

/**
 * author: Matthew Yang on 17/6/6
 * e-mail: yangtian@yy.com
 */

@BrickView("image_and_text")
public class ImageTextView extends FrameLayout {

    private ImageView image;
    private TextView text;

    @BrickEventHandler("image_and_text")
    public IBrickEventHandler brickEventHandler;

    public ImageTextView(Context context) {
        super(context);
        init(context);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View root = inflate(context, R.layout.view_image_text, this);
        image = (ImageView) root.findViewById(R.id.image);
        text = (TextView) root.findViewById(R.id.text);
        image.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                brickEventHandler.handleBrickEvent(0, image);
            }
        });
        image.setOnLongClickListener(new OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                brickEventHandler.handleBrickEvent(1, text);
                return true;
            }
        });
    }

    @SetBrickData("image_and_text")
    public void setData(ImageText imageText) {
        Glide.with(image.getContext()).load(imageText.getUrl()).into(image);
        text.setText(imageText.getContent());
    }


}
