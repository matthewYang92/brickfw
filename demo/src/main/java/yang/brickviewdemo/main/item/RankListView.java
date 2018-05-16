package yang.brickviewdemo.main.item;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yang.brickfw.BrickRecyclerView;
import yang.brickfw.BrickView;
import yang.brickfw.OnRecycled;
import yang.brickfw.SetBrickData;
import yang.brickviewdemo.BrickType;
import yang.brickviewdemo.R;
import yang.brickviewdemo.main.entity.UserInfo;

/**
 * author: Matthew Yang on 17/7/15
 * e-mail: yangtian@yy.com
 */
@BrickView(BrickType.RANK_LIST)
public class RankListView extends FrameLayout {

    @BindView(R.id.recycler)
    BrickRecyclerView recyclerView;

    public RankListView(Context context) {
        super(context);
        init(context);
    }

    public RankListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View root = inflate(context, R.layout.item_rank_list, this);
        ButterKnife.bind(this, root);
        recyclerView.setOrientation(RecyclerView.HORIZONTAL);
    }

    @SetBrickData(BrickType.RANK_LIST)
    void setData(List<UserInfo> userInfoList) {
        recyclerView.setSingleTypeData(BrickType.RANK_ITEM, userInfoList);
    }

    @OnRecycled(BrickType.RANK_LIST)
    void onRecycled() {

    }
}
