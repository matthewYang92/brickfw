package yang.brickviewdemo.main;

import java.util.List;

import yang.brickviewdemo.main.entity.BannerInfo;
import yang.brickviewdemo.main.entity.FeedInfo;
import yang.brickviewdemo.main.entity.UserInfo;
import yang.brickviewdemo.mvp.view.IView;

/**
 * author: Matthew Yang on 17/7/15
 * e-mail: yangtian@yy.com
 */

interface MainIView extends IView {

    void showRankList(List<UserInfo> userInfoList);
    void showBanner(BannerInfo banner);
    void showFeedList(List<FeedInfo> feedInfoList);
}
