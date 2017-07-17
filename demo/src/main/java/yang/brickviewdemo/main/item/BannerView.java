package yang.brickviewdemo.main.item;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;

import yang.brickfw.BrickView;
import yang.brickfw.DecorationInfo;
import yang.brickfw.IDecoration;
import yang.brickfw.SetBrickData;
import yang.brickviewdemo.BrickType;
import yang.brickviewdemo.main.entity.BannerInfo;

/**
 * author: Matthew Yang on 17/7/15
 * e-mail: yangtian@yy.com
 */
@BrickView(BrickType.BANNER)
public class BannerView extends AppCompatImageView implements IDecoration {

    public BannerView(Context context) {
        super(context);
    }

    public BannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @SetBrickData(BrickType.BANNER)
    void setData(BannerInfo bannerInfo) {
        Glide.with(getContext()).load(bannerInfo.getCoverUrl()).asBitmap().centerCrop().into(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, 400);
    }

    @Override
    public void updateDecoration(DecorationInfo decorationInfo) {
        decorationInfo.setDividerBottom(Color.WHITE, 10);
        decorationInfo.setDividerLeft(Color.WHITE, 3);
        decorationInfo.setDividerRight(Color.WHITE, 3);
    }
}
