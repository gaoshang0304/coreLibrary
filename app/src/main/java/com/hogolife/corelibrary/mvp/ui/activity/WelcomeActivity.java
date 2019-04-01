package com.hogolife.corelibrary.mvp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.daydream.corelibrary.app.base.BaseActivity;
import com.daydream.corelibrary.app.dialog.RemindDialog;
import com.daydream.corelibrary.app.mvp.RxPresenter;
import com.daydream.corelibrary.utils.PermissionUtils;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.mvp.ui.MainActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jetbrains.annotations.NotNull;

/**
 * 欢迎页
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-09
 */

public class WelcomeActivity extends BaseActivity {

    private final int ENTER_MAIN = 0x110;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ENTER_MAIN:
                    goToMain();
                    break;
            }
            return false;
        }
    });

    @NotNull
    @Override
    public RxPresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        checkPermission();
    }

    @Override
    public void initData() {

    }

    private void checkPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
//        boolean hasPermissions = PermissionUtils.hasPermissions(this,
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (hasPermissions) {
//            mHandler.sendEmptyMessageDelayed(ENTER_MAIN, 2000);
//        } else {
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        mHandler.sendEmptyMessage(ENTER_MAIN);
                    }
                });
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            goToMain();
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showTipDialog() {
        RemindDialog remindDialog = new RemindDialog(mContext);
        remindDialog.setContent("不允许获取存储权限，是否马上授予？");
        remindDialog.setConfirmText("手动设置");
        remindDialog.setOnButtonClickListener(new RemindDialog.OnButtonClickListener() {
            @Override
            public void onButtonClick(RemindDialog dialog, View view) {
                PermissionUtils.startApplicationDetailsSettings(WelcomeActivity.this, 1000);
            }
        });
        remindDialog.show();
    }

}
