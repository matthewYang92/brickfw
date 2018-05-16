package yang.brickviewdemo.main.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import yang.brickfw.BrickView;
import yang.brickfw.OnRecycled;
import yang.brickfw.SetBrickData;
import yang.brickviewdemo.BrickType;
import yang.brickviewdemo.R;
import yang.brickviewdemo.main.entity.UserInfo;
import yang.brickviewdemo.widget.CircleImageView;

/**
 * author: Matthew Yang on 17/7/15
 * e-mail: yangtian@yy.com
 */
@BrickView(BrickType.RANK_ITEM)
public class RankItemView extends FrameLayout {

    @BindView(R.id.image_head)
    CircleImageView head;
    @BindView(R.id.text_nick)
    TextView nick;

    public RankItemView(Context context) {
        super(context);
        init(context);
    }

    public RankItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View root = inflate(context, R.layout.item_rank, this);
        ButterKnife.bind(this, root);
    }

    @SetBrickData(BrickType.RANK_ITEM)
    void setData(UserInfo userInfo) {
        Glide.with(getContext()).load(userInfo.getHeadUrl()).asBitmap().centerCrop().into(head);
        nick.setText(userInfo.getNick());
    }

    @OnRecycled(BrickType.RANK_ITEM)
    void onRecycled() {

    }
}
