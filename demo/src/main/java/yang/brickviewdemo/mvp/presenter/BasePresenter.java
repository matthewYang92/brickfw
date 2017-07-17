package yang.brickviewdemo.mvp.presenter;


import yang.brickviewdemo.mvp.view.IView;

/**
 * Created by yangtian on 17/4/25.
 */

public class BasePresenter<V extends IView> implements IPresenter<V> {

    private static final String TAG = "BasePresenter";

    private V mMvpView;
    private boolean mIsAttachViewMethodCalled = false;

    @Override
    public void attachView(V mvpView) {
        mIsAttachViewMethodCalled = true;
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    @Override
    public boolean isViewAttached() {
        return mMvpView != null;
    }

    @Override
    public V getMvpView() {
        if (!mIsAttachViewMethodCalled) {
            throw new RuntimeException("please call attachView first");
        }
        return mMvpView;
    }
}
