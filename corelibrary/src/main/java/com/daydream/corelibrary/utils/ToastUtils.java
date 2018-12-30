package com.daydream.corelibrary.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.daydream.corelibrary.R;

/**
 * 吐司工具
 */
public class ToastUtils {

    private static Toast mToast;

    /**
     * 自定义图片toast
     *
     * @param context
     * @param resId   背景图片
     */
    public static void showImage(Context context, int resId) {
        try {
            cancelToast();
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(resId);
            mToast = new Toast(context);
            mToast.setGravity(Gravity.CENTER, 0, -60);
            mToast.setView(imageView);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义布局toast
     *
     * @param context
     * @param layoutId
     */
    public static void showLayout(Context context, int layoutId) {
        try {
            cancelToast();
            View view = View.inflate(context, layoutId, null);
            mToast = new Toast(context);
            mToast.setGravity(Gravity.CENTER, 0, -60);
            mToast.setView(view);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示有image的toast
     * 成功
     */
    public static Toast showToastSuccess(final Context context, @NonNull String text) {
        cancelToast();
        mToast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
        ImageView iv_tip = view.findViewById(R.id.iv_tip);
        iv_tip.setImageResource(R.drawable.ic_tips_success);
        TextView tv = view.findViewById(R.id.tv_tip);
        tv.setText(text);
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, -60);
        mToast.show();
        return mToast;

    }

    /**
     * 显示有image的toast
     * 失败
     */
    public static Toast showToastFailed(final Context context, @NonNull String text) {
        cancelToast();
        mToast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
        ImageView iv_tip = view.findViewById(R.id.iv_tip);
        iv_tip.setImageResource(R.drawable.ic_tips_error);
        TextView tv = view.findViewById(R.id.tv_tip);
        tv.setText(text);
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, -60);
        mToast.show();
        return mToast;

    }

    /**
     * 打印普通toast方法
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg, int duration) {
        try {
            cancelToast();
            mToast = new Toast(context);
            mToast.setText(msg);
            mToast.setDuration(duration);
            mToast.setGravity(Gravity.CENTER, 0, -30);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印普通toast方法
     *
     * @param context
     * @param resId
     */
    public static void showToast(Context context, int resId, int duration) {
        try {
            cancelToast();
            mToast = new Toast(context);
            mToast.setText(resId);
            mToast.setDuration(duration);
            mToast.setGravity(Gravity.CENTER, 0, -30);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印普通toast方法
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 打印普通toast方法
     *
     * @param context
     * @param resId
     */
    public static void showToast(Context context, int resId) {
        showToast(context, resId, Toast.LENGTH_SHORT);
    }

    /**
     * 关闭toast
     */
    public static void cancelToast() {
        try {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}