package com.daydream.corelibrary.weight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daydream.corelibrary.R;

/**
 * 拖拽View
 *
 * @author daydream
 */
public class MapDragView extends RelativeLayout {


    private int iconWidth = 0;
    private int iconHeight = 0;

    private ImageView mImageView;
    private Drawable imageSrc;

    public MapDragView(Context context) {
        super(context);
        init(context,null);
    }

    public MapDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MapDragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setImageSrc(int drawableId){
        imageSrc = getResources().getDrawable(drawableId);
        if(imageSrc != null){
            iconWidth = imageSrc.getIntrinsicWidth();
            iconHeight = imageSrc.getIntrinsicHeight();
        }
    }

    private void init(Context context, AttributeSet attrs) {

        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MapDragView);
            imageSrc = ta.getDrawable(R.styleable.MapDragView_imageSrc);
            if(imageSrc != null){
                iconWidth = imageSrc.getIntrinsicWidth();
                iconHeight = imageSrc.getIntrinsicHeight();
            }
            ta.recycle();
        }

    }

    private void addImageView(Point point) {
        if(mImageView == null) {
            mImageView = new ImageView(getContext());
        }

        if(getChildCount() > 0){
            removeAllViews();
        }

        mImageView.setImageDrawable(imageSrc);

        LayoutParams lp1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp1.leftMargin = point.x - iconWidth / 2;
        lp1.topMargin = point.y - iconHeight;
        addView(mImageView, lp1);
    }

    /**
     * 拖拽回调接口
     *
     * @author wangheng
     */
    public interface Callback{
        /**
         * 得到目标显示位置
         * @return
         */
        Point getAnchorPoint();

        /**
         * 拖拽开始
         * @param x x坐标
         * @param y y坐标
         */
        void onDragStart(int x, int y);

        /**
         * 拖拽到边界
         * @param oldX 拖拽开始的位置 - X坐标
         * @param oldY 拖拽开始的位置 - Y坐标
         * @param x 拖拽的当前位置 - X坐标
         * @param y 拖拽的当前位置 - Y坐标
         * @param suggestX 建议监听者x轴移动的距离，可能是大于0，小于0或者等于0的值
         * @param suggestY 建议监听者y轴移动的距离，可能是大于0，小于0或者等于0的值
         */
        void onEnterEdge(int oldX, int oldY, int x, int y, int suggestX, int suggestY);

        /**
         * 拖拽结束
         * @param x 拖拽结束的X坐标
         * @param y 拖拽结束的Y坐标
         */
        void onDragEnd(int x, int y);
    }

    private Callback mCallback;

    public void setCallback(Callback callback){
        this.mCallback = callback;
    }
    private int downX = 0;
    private int downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(mCallback == null){
                    return false;
                }
                Point p = mCallback.getAnchorPoint();
                if(p == null){
                    return false;
                }

                if(mDragEndAnimator != null && mDragEndAnimator.isRunning()){
                    mDragEndAnimator.cancel();
                }

                downX = x;
                downY = y;

                int halfWidth = (int) (iconWidth / 2.0f);
                int halfHeight = (int) (iconHeight / 2.0f);
                Rect rect = new Rect(p.x - halfWidth, p.y - halfHeight, p.x + halfWidth, p.y + halfHeight);
                if (getChildCount() == 0 && rect.contains(x,y)) {
                    addImageView(p);
                    if(mCallback != null){
                        mCallback.onDragStart(x,y);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mImageView.setX(x - iconWidth / 2);
                mImageView.setY(y - iconHeight);

                if(mCallback != null) {
                    if (x > getWidth() - iconWidth
                            || x < iconWidth
                            || y > getHeight() - iconHeight
                            || y < iconHeight) {

                        int singleX = x > downX ? 1 : -1;
                        int singleY = y > downY ? 1 : -1;

                        mCallback.onEnterEdge(downX,downY,x,y,singleX * iconWidth,singleY * iconHeight);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                mDragEndAnimator = ObjectAnimator.ofFloat(mImageView, "y", mImageView.getY()
                        , mImageView.getY() - iconHeight / 3.0f, mImageView.getY());
                mDragEndAnimator.setDuration(1200);
                mDragEndAnimator.setInterpolator(new BounceInterpolator());
                mDragEndAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        removeAllViews();
                        super.onAnimationCancel(animation);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mCallback != null) {
                            mCallback.onDragEnd(x,y - iconHeight / 4);
                        }
                        removeAllViews();
                        super.onAnimationEnd(animation);
                    }
                });
                mDragEndAnimator.start();
                break;
            default:

                break;
        }
        return getChildCount() > 0;
    }
    private ObjectAnimator mDragEndAnimator = null;

}
