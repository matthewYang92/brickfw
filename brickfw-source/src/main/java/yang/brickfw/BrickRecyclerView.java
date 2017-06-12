package yang.brickfw;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 装填BrickView的容器
 *
 * 使用setData自动装填
 */
public class BrickRecyclerView extends RecyclerView {
    private static final String TAG = "BrickRecyclerView";

    private List<BrickInfo> mCompletedBrickInfoList = new ArrayList<>();

    private BrickRecyclerAdapter mAdapter;
    private SparseArray<BrickPositionInfo> mBrickPositionCache = new SparseArray<>();

    public BrickRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public BrickRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BrickRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public BrickInfo getBrickInfo(int pos) {
        if (mAdapter != null) {
            return mAdapter.getItemData(pos);
        }
        return null;
    }

    /**
     * 设置封装BrickInfo数据
     *
     * @param data
     */
    public void setBrickData(List<BrickInfo> data) {
        mCompletedBrickInfoList = data;
        setCompletedData(mCompletedBrickInfoList);
    }

    /**
     * 设置源数据
     * @param extraMap
     */
    public void setData(Map<String, Object> extraMap) {
        mCompletedBrickInfoList.clear();
        for (String type : extraMap.keySet()) {
            mCompletedBrickInfoList.add(new BrickInfo(type, extraMap.get(type)));
        }
        setCompletedData(mCompletedBrickInfoList);
    }

    public void setOrientation(int orientation) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) getLayoutManager();
        gridLayoutManager.setOrientation(orientation);
    }

    /**
     * 获取RecycleView item 行数
     * @return 行数
     */
    public int getCount(){
       return mAdapter.getItemCount();
    }

    public void clearData(){
        mCompletedBrickInfoList.clear();
        setCompletedData(mCompletedBrickInfoList);
    }

    /**
     * 添加Brick数据
     * @param data
     */
    public void addBrickData(BrickInfo data) {
        mCompletedBrickInfoList.add(data);
        setCompletedData(mCompletedBrickInfoList);
    }

    /**
     * 添加源数据
     * @param type
     * @param extra
     */
    public void addData(String type, Object extra) {
        mCompletedBrickInfoList.add(new BrickInfo(type, extra));
        setCompletedData(mCompletedBrickInfoList);
    }

    /**
     * 添加源数据
     * @param type
     * @param extra
     * @param columns 列数
     */
    public void addData(String type, Object extra, int columns) {
        mCompletedBrickInfoList.add(new BrickInfo(type, extra, columns));
        setCompletedData(mCompletedBrickInfoList);
    }

    private void setCompletedData(List<BrickInfo> data) {
        rebuildPositionCache(data);
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    private void rebuildPositionCache(List<BrickInfo> extendedData) {
        mBrickPositionCache.clear();
        int idxInGroup = 0;
        int groupSize = 0;
        for (int i = 0; i < extendedData.size(); i++) {
            BrickInfo current = extendedData.get(i);
            if (groupSize == 0) {
                groupSize++;
                for (int j = i + 1; j < extendedData.size(); j++) {
                    BrickInfo next = extendedData.get(j);
                    if (next != null && TextUtils.equals(next.getType(), current.getType())) {
                        groupSize++;
                    } else {
                        break;
                    }
                }
            }

            BrickPositionInfo p = new BrickPositionInfo();
            p.setIdxInGlobal(i);
            p.setIdxInGroup(idxInGroup);
            p.setGroupSize(groupSize);
            mBrickPositionCache.put(i, p);

            if (++idxInGroup == groupSize) {
                idxInGroup = 0;
                groupSize = 0;
            }
        }
    }


    public void setEventHandler(Object eventHandler) {
        mAdapter.setEventHandler(eventHandler);
    }

    public void scrollToTop() {
        smoothScrollToPosition(0);
    }

    public void scrollToTopWithNoAnim(){
        scrollToPosition(0);
    }

    /**
     * 根据子View获得对应组件在整个组件集合中的位置
     *
     * @return
     */
    public BrickPositionInfo getBrickPosition(AbstractBrickHolder brick) {
        if (brick != null) {
            int pos = brick.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                return mBrickPositionCache.get(pos);
            }
        }
        return null;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        return false;
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        mAdapter = new BrickRecyclerAdapter();
        setLayoutManager(createLayoutManager(context));
        setAdapter(mAdapter);
        addItemDecoration(new BrickRecyclerItemDecoration(context));
    }

    private LayoutManager createLayoutManager(Context context) {
        final int SPAN_COUNT = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(context, SPAN_COUNT);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int cols = 1;
                BrickInfo info = mAdapter.getItemData(position);
                if (info != null) {
                    cols = info.getColumns();
                }

                if (cols <= 0) {
                    return SPAN_COUNT;
                } else if (cols > SPAN_COUNT) {
                    return 1;
                } else {
                    return SPAN_COUNT / cols;
                }
            }
        });

        return layoutManager;
    }
}
