package com.daydream.corelibrary.utils;

/**
 * 防止快速点击
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-11-27
 */

public class FastClickUtils {

    private static long lastClickTime = 0;//上次点击的时间
    private static int spaceTime = 1000;//时间间隔

    public static boolean isFastClick() {

        long currentTime = System.currentTimeMillis();//当前系统时间
        boolean isAllowClick;//是否允许点击

        if (currentTime - lastClickTime > spaceTime) {
            isAllowClick = false;
        } else {
            isAllowClick = true;
        }
        lastClickTime = currentTime;

        return isAllowClick;

    }

}
