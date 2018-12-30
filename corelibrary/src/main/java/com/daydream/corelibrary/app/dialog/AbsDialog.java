package com.daydream.corelibrary.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.daydream.corelibrary.R;
import com.daydream.corelibrary.app.App;
import com.daydream.corelibrary.utils.DeviceUtils;
import com.daydream.corelibrary.utils.PixelUtils;

/**
 * 自定义Dialog基类
 *
 * @author wangheng
 */
public abstract class AbsDialog extends Dialog {

    private boolean cancelable;

    public AbsDialog(Context context) {
        super(context, R.style.DialogStyle);
        initDialog(context);
    }

    protected AbsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.DialogStyle                              );
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);

        initDialog(context);
    }

    private void initDialog(Context context) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if(window != null) {

            if(window.getDecorView() != null){
                window.getDecorView().setPadding(0,0,0,0);
            }

            WindowManager.LayoutParams lp = window.getAttributes();
            window.setGravity(Gravity.CENTER);

            int screenWidth = DeviceUtils.getScreenWidth(App.getInstance().getContext());

            lp.width = screenWidth - 2 * PixelUtils.dip2px(30); // 宽度
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度

            window.setAttributes(lp);
        }
        this.setCancelable(cancelable);
    }

    @Override
    public void show() {
        if(isShowing()){
            return;
        }
        super.show();
    }

    @Override
    public void dismiss() {
        if(!isShowing()){
            return;
        }
        super.dismiss();
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return !cancelable;
        }
        return super.onKeyDown(keyCode, event);
    }
}
