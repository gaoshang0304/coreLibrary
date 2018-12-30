package com.daydream.corelibrary.app.base;

import android.content.Context;
import android.content.Intent;
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
import com.daydream.corelibrary.utils.DialogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gjc on 2017-05-04.
 * <p>
 * BaseActivity
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;//全局上下文对象
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        unbinder.unbind();
        unbinder = null;
        super.onDestroy();
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
    }

    protected void onViewCreated() {

    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
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
    protected void showLoading(@Nullable String msg) {
        DialogUtils.showLoadingDialog(this, msg);
    }

    /**
     * 隐藏提示框
     */
    protected void hideLoading() {
        DialogUtils.hideLoadingDialog();
    }

    /**
     * @param intent 自己new intent 可以加extra
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.exit_from_right);
    }

    /**
     * @param intent      自己new intent 可以加extra
     * @param requestCode requestCode
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.exit_from_right);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.exit_from_right, R.anim.slide_out_to_right);
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
