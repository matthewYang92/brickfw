package yang.brickviewdemo.main;

import java.util.ArrayList;
import java.util.List;

import yang.brickviewdemo.main.entity.BannerInfo;
import yang.brickviewdemo.main.entity.FeedInfo;
import yang.brickviewdemo.main.entity.UserInfo;
import yang.brickviewdemo.mvp.presenter.BasePresenter;

/**
 * author: Matthew Yang on 17/7/15
 * e-mail: yangtian@yy.com
 */

class MainPresenter extends BasePresenter<MainIView> {
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

    void loadData() {
        getMvpView().showRankList(createTestRankData());
        getMvpView().showBanner(createTestBannerData());
        getMvpView().showFeedList(createTestFeedData());
    }

    private List<UserInfo> createTestRankData() {
        List<UserInfo> userInfoList = new ArrayList<>();
        for (String image : imageUrls) {
            UserInfo userInfo = new UserInfo();
            userInfo.setHeadUrl(image);
            userInfo.setNick("小猫儿");
            userInfoList.add(userInfo);
        }
        return userInfoList;
    }

    private BannerInfo createTestBannerData() {
        BannerInfo bannerInfo = new BannerInfo();
        bannerInfo.setCoverUrl(imageUrls[0]);
        return bannerInfo;
    }

    private List<FeedInfo> createTestFeedData() {
        List<FeedInfo> feedInfoList = new ArrayList<>();
        for (String image : imageUrls) {
            FeedInfo feedInfo = new FeedInfo();
            feedInfo.setCoverUrl(image);
            feedInfoList.add(feedInfo);
        }
        return feedInfoList;
    }

}
