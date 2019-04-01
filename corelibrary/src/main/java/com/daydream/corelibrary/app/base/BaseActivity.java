package com.daydream.corelibrary.app.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.daydream.corelibrary.R;
import com.daydream.corelibrary.app.manager.AppManager;
import com.daydream.corelibrary.app.mvp.IPresenter;
import com.daydream.corelibrary.app.mvp.IView;
import com.daydream.corelibrary.app.transitionmode.TransitionMode;
import com.daydream.corelibrary.utils.DialogUtils;
import com.daydream.corelibrary.weight.MultipleStatusView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gjc on 2017-05-04.
 * <p>
 * BaseActivity
 */

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView {

    protected Context mContext;//全局上下文对象
    private Unbinder unbinder;
    protected MultipleStatusView mMultipleStatusView = null;
    protected P mPresenter = initPresenter();
    private TransitionMode mTransitionMode = TransitionMode.RIGHT;

    protected abstract P initPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {

        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        mContext = getApplicationContext();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView(savedInstanceState);
        onViewCreated();
        initData();
        AppManager.getAppManager().addActivity(this);
        TransitionMode mode = setOverridePendingTransitionMode(mTransitionMode);
        if (mode.equals(TransitionMode.LEFT)) {
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        } else if (mode.equals(TransitionMode.RIGHT)) {
            overridePendingTransition(R.anim.enter_trans, R.anim.exit_right);
        }else if (mode.equals(TransitionMode.TOP)) {
            overridePendingTransition(R.anim.top_in, R.anim.top_out);
        }else if (mode.equals(TransitionMode.BOTTOM)) {
            overridePendingTransition(R.anim.bottom_in, 0);
        }else if (mode.equals(TransitionMode.SCALE)) {
            overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
        }else if (mode.equals(TransitionMode.FADE)) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }else if (mode.equals(TransitionMode.ZOOM)) {
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    }

    protected void onViewCreated() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 初始化数据
     * <p>
     * 子类可以复写此方法初始化子类数据
     */
    protected void initData(){}

    /**
     * 初始化view
     * <p>
     * 子类实现 控件绑定、视图初始化等内容
     *
     * @param savedInstanceState savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 获取当前layouty的布局ID,用于设置当前布局
     * <p>
     * 交由子类实现
     *
     * @return layout Id
     */
    protected abstract int getLayoutId();

    /**
     * 显示提示框
     */
    public void showLoading(@Nullable String msg) {
        DialogUtils.showLoadingDialog(this, msg);
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

    protected TransitionMode setOverridePendingTransitionMode(TransitionMode transitionMode) {
        mTransitionMode = transitionMode;
        return mTransitionMode;
    }

    @Override
    public void finish() {
        super.finish();
        TransitionMode mode = setOverridePendingTransitionMode(mTransitionMode);
        if (mode.equals(TransitionMode.LEFT)) {
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        } else if (mode.equals(TransitionMode.RIGHT)) {
            overridePendingTransition(R.anim.exit_right, R.anim.exit_trans);
        }else if (mode.equals(TransitionMode.TOP)) {
            overridePendingTransition(R.anim.top_in, R.anim.top_out);
        }else if (mode.equals(TransitionMode.BOTTOM)) {
            overridePendingTransition(0, R.anim.bottom_out);
        }else if (mode.equals(TransitionMode.SCALE)) {
            overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
        }else if (mode.equals(TransitionMode.FADE)) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }else if (mode.equals(TransitionMode.ZOOM)) {
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroy();
    }


    /**
     * 隐藏键盘
     *
     * @return 隐藏键盘结果
     * <p>
     * true:隐藏成功
     * <p>
     * false:隐藏失败
     */
    protected boolean hiddenKeyboard() {
        //点击空白位置 隐藏软键盘
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService
                (INPUT_METHOD_SERVICE);
        return mInputMethodManager.hideSoftInputFromWindow(this
                .getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN  &&
                getCurrentFocus()!=null &&
                getCurrentFocus().getWindowToken()!=null) {

            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, event)) {
                hiddenKeyboard();
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationOnScreen(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

}
