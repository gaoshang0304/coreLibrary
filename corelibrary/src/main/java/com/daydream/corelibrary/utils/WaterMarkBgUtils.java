package com.daydream.corelibrary.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.daydream.corelibrary.R;

/**
 * 水印
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-05-05
 */

public class WaterMarkBgUtils {


    //设置背景
    public static void setWaterMarkTextBg(Context context, View view, String text) {
        view.setBackground(drawTextToBitmap(context, text));
    }

    //设置背景
    public static void setWaterMarkTextBg(View view, String gText, int bgColor, int strColor, int strSize) {
        view.setBackground(drawTextToBitmap(gText, bgColor, strColor, strSize));
    }

    /**
     * 生成水印文字图片
     */
    private static Drawable drawTextToBitmap(String gText, int bgColor, int strColor, int strSize) {
        return new WaterMarkBg(gText, bgColor, strColor, strSize);
    }

    /**
     * 生成水印文字图片
     */
    private static Drawable drawTextToBitmap(Context context, String gText) {
        return new WaterMarkBg(gText, context.getResources().getColor(R.color.color_white),
                context.getResources().getColor(R.color.color_text_sub),
                (int) context.getResources().getDimension(R.dimen.sp_15));
    }

    private static class WaterMarkBg extends Drawable {

        private String mText;
        private int mBgColor;
        private int mStrColor;
        private int mStrSize;
        Paint mPaint = new Paint();

        public WaterMarkBg(String text, int bgColor, int strColor, int strSize) {
            mText = text;
            mBgColor = bgColor;
            mStrColor = strColor;
            mStrSize = strSize;
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            int width = getBounds().right;
            int height = getBounds().bottom;
            //背景颜色
            canvas.drawColor(mBgColor);
            //文字的设置
            mPaint.setColor(mStrColor);
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(mStrSize);
            canvas.save();
            //旋转角度
            canvas.rotate(-20);
            //测量文字的宽度
            float textWidth = mPaint.measureText(mText);
            int index = 0;
            //height / num ; num 为水印的行数
            for (int positionY = height / 12; positionY <= height; positionY += height / 12) {
                float fromX = -width + (index++ % 2) * textWidth;
                for (float positionX = fromX; positionX < width; positionX += textWidth * 2) {
                    canvas.drawText(mText, positionX, positionY, mPaint);
                }
            }
            canvas.restore();
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    }

}
