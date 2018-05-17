package yang.brickviewdemo.main.item;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import com.bumptech.glide.Glide;

import yang.brickfw.BrickView;
import yang.brickfw.DecorationInfo;
import yang.brickfw.IDecoration;
import yang.brickfw.OnRecycled;
import yang.brickfw.SetBrickData;
import yang.brickviewdemo.BrickType;
import yang.brickviewdemo.main.entity.FeedInfo;

/**
 * author: Matthew Yang on 17/7/15
 * e-mail: yangtian@yy.com
 */

@BrickView(BrickType.FEED_ITEM)
public class FeedView extends AppCompatImageView implements IDecoration {

    public FeedView(Context context) {
        super(context);
    }

    public FeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SetBrickData(BrickType.FEED_ITEM)
    void setData(FeedInfo feedInfo) {
        Glide.with(getContext()).load(feedInfo.getCoverUrl()).asBitmap().centerCrop().into(this);
    }

    @OnRecycled(BrickType.FEED_ITEM)
    void onRecycled() {
        Log.v("Feed", "onRecycled");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    /**
     * 分割线设置
     * @param decorationInfo
     */
    @Override
    public void updateDecoration(DecorationInfo decorationInfo) {
        decorationInfo.setDividerLeft(Color.WHITE, 3);
        decorationInfo.setDividerRight(Color.WHITE, 3);
        decorationInfo.setDividerTop(Color.WHITE, 3);
        decorationInfo.setDividerBottom(Color.WHITE, 3);
    }
}
