package yang.brickfw;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;
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
    public void setBrickList(List<BrickInfo> data) {
        mCompletedBrickInfoList = data;
        setCompletedData(mCompletedBrickInfoList);
    }

    /**
     * 设置单一类型数据列表
     * @param type
     * @param datas
     */
    public void setSingleTypeData(String type, List<? extends Object> datas) {
        List<BrickInfo> infos = new ArrayList<>();
        for (Object data : datas) {
            infos.add(new BrickInfo(type, data));
        }
        setBrickList(infos);
    }

    /**
     * 添加Brick数据列表
     * @param data
     */
    public void addBrickList(List<BrickInfo> data) {
        mCompletedBrickInfoList.addAll(data);
        setCompletedData(mCompletedBrickInfoList);
    }

    /**
     * 添加单一类型列表
     * @param datas
     */
    public void addSingleDataList(String type, List<? extends Object> datas) {
        addSingleDataList(type, datas, 1);
    }

    /**
     * 添加单一类型列表
     * @param datas
     */
    public void addSingleDataList(String type, List<? extends Object> datas, int columns) {
        for (Object data : datas) {
            mCompletedBrickInfoList.add(new BrickInfo(type, data, columns));
        }
        setCompletedData(mCompletedBrickInfoList);
    }

    /**
     * 添加Brick数据
     * @param data
     */
    public void addBrick(BrickInfo data) {
        mCompletedBrickInfoList.add(data);
        setCompletedData(mCompletedBrickInfoList);
    }

    /**
     * 添加源数据
     * @param type
     * @param extra
     */
    public void addData(String type, Object extra) {
        addBrick(new BrickInfo(type, extra));
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

    public void setOrientation(int orientation) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) layoutManager).setOrientation(orientation);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) layoutManager).setOrientation(orientation);
        }
    }

    /**
     * 获取RecycleView item 行数
     * @return 行数
     */
    public int getCount(){
       return mAdapter.getItemCount();
    }

    public void clear(){
        mCompletedBrickInfoList.clear();
        setCompletedData(mCompletedBrickInfoList);
    }

    public void update() {
        mAdapter.notifyDataSetChanged();
    }

    public void updateRange(int pos, int itemCount) {
        mAdapter.notifyItemRangeChanged(pos, itemCount);
    }

    public void updateItem(int pos) {
        mAdapter.notifyItemChanged(pos);
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
            current.setPositionInfo(p);

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
        setLayoutManager(createLayoutManager(context, 1));
        setAdapter(mAdapter);
        addItemDecoration(new BrickRecyclerItemDecoration());
    }

    /**
     * 设置瀑布流布局
     * @param columns
     * @param orientation
     */
    public void setStaggeredLayout(int columns, int orientation) {
        LayoutManager layoutManager = getLayoutManager();
        if (null != layoutManager) {
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                staggeredGridLayoutManager.setSpanCount(columns);
                return;
            }
        }
        setLayoutManager(createStaggeredGridLayoutManager(columns, orientation));
    }

    private LayoutManager createStaggeredGridLayoutManager(int columns, int orientation) {
        return new StaggeredGridLayoutManager(columns, orientation);
    }

    /**
     * 设置普通布局
     * @param context
     * @param spanSize 占位大小 把一行分为spanSize个位置
     */
    public void setNormalLayout(Context context, int spanSize) {
        LayoutManager layoutManager = getLayoutManager();
        if (null != layoutManager) {
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                setGridLayoutColumns(gridLayoutManager, spanSize);
                return;
            }
        }
        setLayoutManager(createLayoutManager(context, spanSize));
    }

    private LayoutManager createLayoutManager(Context context, final int spanSize) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, spanSize);
        setGridLayoutColumns(layoutManager, spanSize);
        return layoutManager;
    }

    private void setGridLayoutColumns(GridLayoutManager layoutManager, final int spanSize) {
        layoutManager.setSpanCount(spanSize);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int cols = 1;
                BrickInfo info = mAdapter.getItemData(position);
                if (info != null) {
                    cols = info.getColumns();
                }

                if (cols <= 0) {
                    return spanSize;
                } else if (cols > spanSize) {
                    return 1;
                } else {
                    return spanSize / cols;
                }
            }
        });
    }
}
