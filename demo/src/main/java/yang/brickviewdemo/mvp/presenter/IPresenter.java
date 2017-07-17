package yang.brickviewdemo.mvp.presenter;


import yang.brickviewdemo.mvp.view.IView;

/**
 * Created by yangtian on 17/4/25.
 */

public interface IPresenter<V extends IView> {

    void attachView(V mvpView);

    void detachView();

    boolean isViewAttached();

    V getMvpView();
}
