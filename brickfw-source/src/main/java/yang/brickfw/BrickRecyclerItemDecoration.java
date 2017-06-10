package yang.brickfw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

class BrickRecyclerItemDecoration extends RecyclerView.ItemDecoration {

    private ColorDrawable mDivider = new ColorDrawable();
    private DecorationInfo mDecorationInfo = new DecorationInfo();
    private Context context;

    public BrickRecyclerItemDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        draw(c, parent);
    }

    private void draw(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();

        mDivider.setColor(Color.BLACK);

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (child == null) {
                continue;
            }

            final AbstractBrickHolder childHolder = (AbstractBrickHolder) parent.getChildViewHolder(child);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            mDecorationInfo.reset();
            mDecorationInfo.position = ((BrickRecyclerView) parent).getBrickPosition(childHolder);
            if (mDecorationInfo.position == null) {
                mDecorationInfo.position = new BrickPositionInfo();
            }
            mDecorationInfo.top = child.getBottom() + params.bottomMargin;
            mDecorationInfo.bottom = mDecorationInfo.top;
            mDecorationInfo.left = child.getRight() + params.rightMargin;
            mDecorationInfo.right = mDecorationInfo.left;

            //if (childHolder != null) {
            //    childHolder.updateDecoration(mDecorationInfo);
            //}

            mDivider.setBounds(child.getLeft() - params.leftMargin, mDecorationInfo.top,
                    child.getRight() + params.rightMargin , mDecorationInfo.bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        super.getItemOffsets(outRect, view, parent, state);

        AbstractBrickHolder childHolder = (AbstractBrickHolder) parent.getChildViewHolder(view);

        mDecorationInfo.reset();
        mDecorationInfo.position = ((BrickRecyclerView) parent).getBrickPosition(childHolder);
        if (mDecorationInfo.position == null) {
            mDecorationInfo.position = new BrickPositionInfo();
        }
        mDecorationInfo.outLeft = 0;
        mDecorationInfo.outTop = 0;
        mDecorationInfo.outRight = 0;
        mDecorationInfo.outBottom = 0;

        //if (childHolder != null) {
        //    childHolder.updateDecoration(mDecorationInfo);
        //}

        outRect.set(mDecorationInfo.outLeft + mDecorationInfo.leftMargin,
                mDecorationInfo.outTop + mDecorationInfo.topMargin,
                mDecorationInfo.outRight + mDecorationInfo.rightMargin,
                mDecorationInfo.outBottom + mDecorationInfo.bottomMargin);
    }
}
