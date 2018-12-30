package com.daydream.corelibrary.utils;

import com.daydream.corelibrary.app.App;

/**
 * 像素工具
 *
 * @author wangheng
 */
public class PixelUtils {
    /**
     * dip转换成px
     *
     * @param dipValue dp
     * @return px
     */
    public static int dip2px(float dipValue) {
        try {
            final float scale = App.getInstance().getResources()
                    .getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("PixelUtils", "dip2px occur Exception::" + e);
        }
        return (int) dipValue;
    }

    /**
     * px 转换成dip
     *
     * @param pxValue px
     * @return dp
     */
    public static int px2dip(float pxValue) {
        final float scale = App.getInstance().getResources()
                .getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * PX转换成SP
     *
     * @param pxValue px
     * @return sp
     */
    public static int px2sp(float pxValue) {
        final float fontScale = App.getInstance().getResources()
                .getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * SP转换为PX
     *
     * @param spValue sp
     * @return px
     */
    public static int sp2px(float spValue) {
        final float fontScale = App.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
