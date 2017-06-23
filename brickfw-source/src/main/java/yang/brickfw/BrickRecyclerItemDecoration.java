package yang.brickfw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

class BrickRecyclerItemDecoration extends RecyclerView.ItemDecoration {

    Paint paint = new Paint();

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        draw(c, parent);
    }

    private void draw(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (child == null) {
                continue;
            }
            final AbstractBrickHolder childHolder = (AbstractBrickHolder) parent.getChildViewHolder(child);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            BrickPositionInfo position = ((BrickRecyclerView) parent).getBrickPosition(childHolder);
            if (position == null) {
                position = new BrickPositionInfo();
            }
            DecorationInfo mDecorationInfo = new DecorationInfo(
                    child.getLeft() - params.leftMargin
                    , child.getRight() + params.rightMargin
                    , child.getTop() - params.topMargin
                    , child.getBottom() + params.bottomMargin
                    , position);

            IDecoration decoration = null;
            if (childHolder instanceof IDecoration) {
                decoration = (IDecoration) childHolder;
            } else if (child instanceof IDecoration) {
                decoration = (IDecoration) child;
            }
            if (null != decoration) {
                decoration.updateDecoration(mDecorationInfo);
                for (DecorationInfo.DecorationItemInfo itemInfo : mDecorationInfo.getDecorationItems()) {
                    paint.setColor(itemInfo.getDrawableColor());
                    c.drawRect(itemInfo.getLeft() + itemInfo.getLeftMargin()
                            , itemInfo.getTop() + itemInfo.getTopMargin()
                            , itemInfo.getRight() - itemInfo.getRightMargin()
                            , itemInfo.getBottom() - itemInfo.getBottomMargin(), paint);
                }
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        super.getItemOffsets(outRect, view, parent, state);
        AbstractBrickHolder childHolder = (AbstractBrickHolder) parent.getChildViewHolder(view);
        BrickPositionInfo position = ((BrickRecyclerView) parent).getBrickPosition(childHolder);
        if (position == null) {
            position = new BrickPositionInfo();
        }
        DecorationInfo mDecorationInfo = new DecorationInfo(position);
        IDecoration decoration = null;
        if (childHolder instanceof IDecoration) {
            decoration = (IDecoration) childHolder;
        } else if (view instanceof IDecoration) {
            decoration = (IDecoration) view;
        }
        if (null != decoration) {
            decoration.updateDecoration(mDecorationInfo);
            outRect.set(mDecorationInfo.getLeftPadding()
                    , mDecorationInfo.getTopPadding()
                    , mDecorationInfo.getRightPadding()
                    , mDecorationInfo.getBottomPadding());
        }
    }
}
