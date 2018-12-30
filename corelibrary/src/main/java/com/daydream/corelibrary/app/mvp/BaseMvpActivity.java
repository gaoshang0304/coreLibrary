package com.daydream.corelibrary.app.mvp;

import com.daydream.corelibrary.app.base.BaseActivity;
import com.daydream.corelibrary.utils.ToastUtils;

/**
 * @author gjc
 * @version ;;
 * @since 2018-04-02
 */

public abstract class BaseMvpActivity<T extends IPresenter> extends BaseActivity implements IView {

    protected T mPresenter = initPresenter();

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorMsg(String msg) {
        ToastUtils.showToast(mContext, msg);
    }

    @Override
    public void stateError() {

    }

    @Override
    public void stateEmpty() {

    }

    @Override
    public void stateLoading() {

    }

    @Override
    public void stateMain() {

    }

    /**
     * 在子类中初始化对应的presenter
     *
     * @return 相应的presenter
     */
    public abstract T initPresenter();
}
