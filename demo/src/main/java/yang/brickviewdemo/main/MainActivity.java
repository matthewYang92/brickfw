package yang.brickviewdemo.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yang.brickfw.BrickInfo;
import yang.brickfw.BrickRecyclerView;
import yang.brickfw.OnBrickEvent;
import yang.brickfw.OnBrickItemClick;
import yang.brickviewdemo.BrickType;
import yang.brickviewdemo.R;
import yang.brickviewdemo.main.entity.BannerInfo;
import yang.brickviewdemo.main.entity.FeedInfo;
import yang.brickviewdemo.main.entity.UserInfo;

public class MainActivity extends AppCompatActivity implements MainIView {

    @BindView(R.id.recycler)
    BrickRecyclerView recyclerView;

    private MainPresenter mainPresenter = new MainPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainPresenter.attachView(this);
        initView();
        loadData();
    }

    private void initView() {
        recyclerView.setNormalLayout(this, 2); //设置布局占位size
        recyclerView.setEventHandler(this);    //设置事件响应
        recyclerView.setDefaultAnimator(true); //开启局部刷新默认动画
    }

    private void loadData() {
        mainPresenter.loadData();
    }

    @Override
    public void showRankList(List<UserInfo> userInfoList) {
        recyclerView.addData(BrickType.RANK_LIST, userInfoList);
    }

    @Override
    public void showBanner(BannerInfo banner) {
        recyclerView.addData(BrickType.BANNER, banner);
    }

    @Override
    public void showFeedList(List<FeedInfo> feedInfoList) {
        recyclerView.addSingleDataList(BrickType.FEED_ITEM, feedInfoList, 2);
    }

    @OnBrickItemClick(BrickType.FEED_ITEM)
    void onClickFeedItem(BrickInfo info, View view) {
        recyclerView.removeBrickInfoPartial(info);
    }

    @OnBrickEvent(value = BrickType.FEED_ITEM, eventType = 0)

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }
}
