package com.daydream.corelibrary.app.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.daydream.corelibrary.app.base.BaseFragment;
import com.daydream.corelibrary.utils.DialogUtils;
import com.daydream.corelibrary.utils.ToastUtils;

/**
 * @author gjc
 * @version ;;
 * @since 2018-04-02
 */

public abstract class BaseMvpFragment<T extends IPresenter> extends BaseFragment implements IView {

    protected T mPresenter = initPresenter();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroyView();
    }

    @Override
    public void showLoading(String msg) {
        DialogUtils.showLoadingDialog(mContext, msg);
    }

    @Override
    public void hideLoading() {
        DialogUtils.hideLoadingDialog();
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
