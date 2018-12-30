package com.daydream.corelibrary.app.mvp;

import com.daydream.corelibrary.app.base.BaseToolBarActivity;
import com.daydream.corelibrary.utils.ToastUtils;

/**
 * class name
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-10-09
 */

public abstract class BaseMvpToolBarActivity<T extends IPresenter> extends BaseToolBarActivity implements IView {


    protected T mPresenter = initPresenter();

    /**
     * 在子类中初始化对应的presenter
     *
     * @return 相应的presenter
     */
    protected abstract T initPresenter();

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
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
}
