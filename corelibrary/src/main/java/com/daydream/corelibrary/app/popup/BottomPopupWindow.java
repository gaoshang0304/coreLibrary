package com.daydream.corelibrary.app.popup;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.daydream.corelibrary.R;

/**
 * 底部显示PopupWindow
 *
 * @author daydream
 */
public class BottomPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    private Activity mActivity;
    private boolean mBackgroundGray = true;

    public BottomPopupWindow(Activity context, View view) {
        mActivity = context;
        // 设置SelectPicPopupWindow的View
        setContentView(view);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupBottomTranslate);

        super.setOnDismissListener(BottomPopupWindow.this);
    }

    public void show(View parent) {
        if (mBackgroundGray) {
            setBackgroundAlpha(0.5f);
        }
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);

    }

    private OnDismissListener mOnDismissListener;

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
        if (this.mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }

    private void setBackgroundAlpha(float bgAlpha) {
        if (mActivity == null || mActivity.getWindow() == null) {
            return;
        }
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        mActivity.getWindow().setAttributes(lp);
    }

    public void setBackgroundGray(boolean needTranslucent) {
        this.mBackgroundGray = needTranslucent;
    }
}
