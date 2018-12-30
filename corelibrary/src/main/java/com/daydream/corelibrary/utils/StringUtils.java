package com.daydream.corelibrary.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import com.daydream.corelibrary.app.App;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类.(对TextUtils的补充)
 *
 * @author daydream
 */
public class StringUtils {
    /**
     * 手机号中间四位变*
     */
    public static String transPhoneNumber(String phone) {
        String newPhone = "";
        try {
            int length = phone.length();
            if (length == 11) {
                try {
                    newPhone = phone.substring(0, 3) + "****" + phone.substring(length - 4, length);
                } catch (Exception e) {
                    return phone;
                }
            }
            return newPhone;
        } catch (Exception e) {

        }
        return phone;
    }

    /**
     * spannableString
     * 设置文字颜色
     */
    public static String spanTextColor(String text, int start, int end, int color) {
        String newStr = "";
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        builder.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        newStr = builder.toString();
        return newStr;
    }

    /**
     * 是否手机号码
     * @param mobiles 字符串
     * @return 是否手机号码
     */
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(14[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isChineseName(String name){
        Pattern p = Pattern.compile("^([\\u4e00-\\u9fa5]{1,20}|[a-zA-Z\\.\\s]{1,20})$");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    public static String formatSeconds(int seconds){
        int hour = seconds / 3600;
        int modeHour = seconds % 3600;
        int minute = modeHour / 60;
        int second = modeHour % 60;

        String result = "";
        if(hour > 0){
            result = hour + ":";
        }
        if(minute < 10){
            result = result + "0" + minute + ":";
        }else{
            result = result + minute + ":";
        }

        if(second < 10){
            result = result + "0" + second;
        }else{
            result = result + second;
        }
        return result;
    }

    public static String getFormatMoney(double money){
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setGroupingUsed(false);
        return nf.format(money);
    }

    public static void copyTextToClipboard(String text){
        ClipboardManager cm = (ClipboardManager) App.getInstance().getContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(null, String.valueOf(text)));
    }
}
