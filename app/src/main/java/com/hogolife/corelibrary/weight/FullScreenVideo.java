package com.hogolife.corelibrary.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * 全屏video
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-10
 */

public class FullScreenVideo extends VideoView {

    public FullScreenVideo(Context context) {
        super(context);
    }

    public FullScreenVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
