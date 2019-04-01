package com.hogolife.corelibrary.utils;

import android.support.annotation.Nullable;

/**
 * Created by dayDream on 2018/4/6.
 */

public class FormatTimeUtils {

    /**
     * 格式化视频时长 分'秒''
     */
    public static String durationFormat(long duration) {
        long minute = duration / 60;
        long second = duration % 60;
        String result = "";
        if (minute <= 9) {
            if (second <= 9) {
                result = "0" + minute + "' " + "0" + second + "''";
            } else {
                result = "0" + minute + "' " + second + "''";
            }
        } else {
            if (second <= 9) {
                result = minute + "' " + "0" + second + "''";
            } else {
                result = minute + "' " + second + "''";
            }
        }
        return result;
    }

    /**
     * 干货集中营 切割标准时间
     * @param time
     * @return
     */
    @Nullable
    public static String subStandardTime(String time) {
        int idx = time.indexOf("T");
        if (idx > 0) {
            return time.substring(0, idx);
        }
        return null;
    }
}
