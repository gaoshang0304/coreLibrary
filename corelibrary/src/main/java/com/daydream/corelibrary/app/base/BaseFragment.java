package com.daydream.corelibrary.app.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.daydream.corelibrary.R;
import com.daydream.corelibrary.app.mvp.IPresenter;
import com.daydream.corelibrary.app.mvp.IView;
import com.daydream.corelibrary.utils.DialogUtils;
import com.daydream.corelibrary.weight.MultipleStatusView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gjc on 2018-05-03.
 * <p>
 */

public abstract class BaseFragment<T extends IPresenter> extends Fragment implements IView {

    protected String TAG;
    protected Context mContext;
    protected Activity mActivity;
    private Unbinder binder;
    protected MultipleStatusView mMultipleStatusView = null;

    protected boolean isViewCreated = false;
    private boolean isVisible = false;

    protected T mPresenter = initPresenter();

    protected abstract T initPresenter();

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //解决长时间后台，fragment重叠
        try {
            if (savedInstanceState != null) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStackImmediate(null, 1);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (getLayoutView() != null) {
            return getLayoutView();
        } else {
            return inflater.inflate(getLayoutId(), container, false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //使内容占据整个屏幕，状态栏字体颜色为黑色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TAG = getClass().getSimpleName();
        binder = ButterKnife.bind(this, view);
        initView(view, savedInstanceState);
        getBundle(getArguments());
        isViewCreated = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
        }
    }

    private void lazyLoad() {
        //去掉了是否第一次加载去请求，可以每次切换刷新
        if (isViewCreated && isVisible) {
            loadData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isVisible = false;
        }
    }

    protected void loadData(){}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binder != null) {
            binder.unbind();
            binder = null;
        }
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @LayoutRes
    public abstract int getLayoutId();

    public View getLayoutView() {
        return null;
    }

    /**
     * 得到Activity传进来的值
     */
    public void getBundle(Bundle bundle) {
    }

    /**
     * 初始化UI
     */
    public abstract void initView(View view, @Nullable Bundle savedInstanceState);

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        InputMethodManager inputMethodManager = ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE));
        if (inputMethodManager != null && getActivity().getCurrentFocus() != null && getActivity().getCurrentFocus().getWindowToken() != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示提示框
     */
    public void showLoading(String msg) {
        DialogUtils.showLoadingDialog(mContext, msg);
    }

    /**
     * 隐藏提示框
     */
    public void hideLoading() {
        DialogUtils.hideLoadingDialog();
    }

    @Override
    public void stateError() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showError();
        }
    }

    @Override
    public void stateNoNetWork() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showNoNetwork();
        }
    }

    @Override
    public void stateEmpty() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showEmpty();
        }
    }

    @Override
    public void stateLoading() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showLoading();
        }
    }

    @Override
    public void stateMain() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showContent();
        }
    }

    public void exitSelf() {
        FragmentManager sfm = getFragmentManager();
        FragmentTransaction sft = sfm.beginTransaction();
        sft.setCustomAnimations(R.anim.bottom_in, R.anim.bottom_out);
        sft.remove(this);
        sft.commit();
    }

}
