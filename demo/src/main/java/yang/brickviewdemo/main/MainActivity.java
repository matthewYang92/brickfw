package yang.brickviewdemo.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import yang.brickfw.BrickInfo;
import yang.brickfw.BrickRecyclerView;
import yang.brickfw.OnBrickItemClick;
import yang.brickviewdemo.BrickType;
import yang.brickviewdemo.R;
import yang.brickviewdemo.main.entity.BannerInfo;
import yang.brickviewdemo.main.entity.FeedInfo;
import yang.brickviewdemo.main.entity.UserInfo;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    String[] imageUrls = {"http://seopic.699pic.com/photo/00001/2480.jpg_wh1200.jpg"
            , "http://seopic.699pic.com/photo/00001/3971.jpg_wh1200.jpg"
            , "http://seopic.699pic.com/photo/00032/2057.jpg_wh1200.jpg"
            , "http://seopic.699pic.com/photo/00030/7825.jpg_wh1200.jpg"
            , "http://seopic.699pic.com/photo/00003/6440.jpg_wh1200.jpg"
            , "http://seopic.699pic.com/photo/00011/3770.jpg_wh1200.jpg"
            , "http://seopic.699pic.com/photo/00037/0500.jpg_wh1200.jpg"
            , "http://seopic.699pic.com/photo/00023/7552.jpg_wh1200.jpg"
            , "http://seopic.699pic.com/photo/00028/9288.jpg_wh1200.jpg"
            , "http://seopic.699pic.com/photo/50005/3196.jpg_wh1200.jpg"};

    @BindView(R.id.recycler)
    BrickRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        setBrickData();
    }

    private void initView() {
        recyclerView.setNormalLayout(this, 2); //设置布局占位size
        recyclerView.setEventHandler(this);    //设置事件响应
        recyclerView.setDefaultAnimator(true); //开启局部刷新默认动画
    }

    private void setBrickData() {
        List<BrickInfo> brickData = new ArrayList<>();
        brickData.add(createBannerBrick(imageUrls[4]));  //添加Banner item
        brickData.add(createBannerBrick(imageUrls[2]));  //添加Banner item
        brickData.add(createRankListBrick()); //添加排行榜item
        for (String url : imageUrls) {
            brickData.add(createFeedItemBrick(url));
        }
        recyclerView.setBrickList(brickData);
    }

    private BrickInfo createFeedItemBrick(String url) {
        BrickInfo info = new BrickInfo(BrickType.FEED_ITEM);
        FeedInfo feedInfo = new FeedInfo();
        feedInfo.setCoverUrl(url);
        info.setExtra(feedInfo);
        info.setColumns(2);  //设置一行显示两个
        return info;
    }

    private BrickInfo createBannerBrick(String url) {
        BrickInfo info = new BrickInfo(BrickType.BANNER);
        BannerInfo bannerInfo = new BannerInfo();
        bannerInfo.setCoverUrl(url);
        info.setExtra(bannerInfo);
        return info;
    }

    private BrickInfo createRankListBrick() {
        List<UserInfo> userInfoList = new ArrayList<>();
        for (int i = 0; i < imageUrls.length; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setHeadUrl(imageUrls[i]);
            userInfo.setNick("小猫儿" + i);
            userInfoList.add(userInfo);
        }
        BrickInfo info = new BrickInfo(BrickType.RANK_LIST);
        info.setExtra(userInfoList);
        return info;
    }

    @OnBrickItemClick(BrickType.FEED_ITEM)
    void onClickFeedItem(BrickInfo info, View view) { //FeedView点击事件的回调
        recyclerView.removeBrickInfoPartial(info);
    }

    @OnBrickItemClick(BrickType.BANNER)
    void onClickBanner(BrickInfo info, View view) {  //BannerView点击事件的回调
        Toast.makeText(this, "BannerItem onClick.", Toast.LENGTH_LONG).show();
        recyclerView.updateItem(1, 1);
    }
}
