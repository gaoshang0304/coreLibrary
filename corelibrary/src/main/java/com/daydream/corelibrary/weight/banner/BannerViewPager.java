package com.daydream.corelibrary.weight.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.daydream.corelibrary.app.App;
import com.daydream.corelibrary.utils.Logger;

import java.lang.reflect.Field;

/**
 * 专为Banner的ViewPager而存在.
 *
 * @author wangheng
 */
public class BannerViewPager extends ViewPager {


    private static final String TAG = "Banner";
    private static final boolean DEBUG = App.getInstance().isDebug();

    private static final int DURATION_DEFAULT = 750;
    /**
     * 自动滚动的时候需要发的消息 *
     */
    private static final int HANDLE_AUTO_SCROLL = 0x1001;

    /**
     * 手指是否停留在ViewPager上 *
     */
    private boolean isTouched = false;

    /**
     * 手指按下的X轴坐标 *
     */
    private float downX;

    /**
     * 手指按下的Y轴坐标 *
     */
    private float downY;

    /**
     * 为了控制自动滚动速度，而自定义的Scroller *
     */
    private CustomScroller scroller;

    /**
     * 自动滚动的时间间隔，默认是5000毫秒 *
     */
    private long autoScrollInterval = 5000;

    /**
     * 滚动到下一页需要的时间 *
     */
//    private int mDuration = DURATION_DEFAULT;

    /**
     * 是否可以自动滚动 *
     */
    private boolean isAutoScrollable = false;

    /**
     * 自动滚动的Handler信息 *
     */
    private AutoScrollHandler mHandler = new AutoScrollHandler(BannerViewPager.this);

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BannerViewPager(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * init:初始化ViewPager. <br/>
     *
     * @param context context
     * @param attrs attrs
     */
    private void init(Context context, AttributeSet attrs) {
        setViewPagerScroller();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        }catch(Throwable ex){
            ex.printStackTrace();
        }
        return false;
    }

    private ViewParent mScrollableParentView;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findScrollableParentView();
    }

    @Override
    protected void onDetachedFromWindow() {
        mScrollableParentView = null;
        if(mHandler != null){
            mHandler.setViewPager(null);
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDetachedFromWindow();
    }

    private void findScrollableParentView() {
        ViewParent vp = getParent();
        while(vp != null){
            if(vp instanceof ViewPager
                    || vp instanceof RecyclerView
                    || vp instanceof ListView
                    || vp instanceof ScrollView){
                mScrollableParentView = vp;
                return;
            }
            vp = vp.getParent();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            isTouched = true;
            stopAutoScrollInternal();
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            isTouched = false;
            startAutoScrollInternal();
        }
        try {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(mScrollableParentView != null) {
                        downX = getSecureX(ev);
                        downY = getSecureY(ev);
                        mScrollableParentView.requestDisallowInterceptTouchEvent(true);
                        setClickable(true);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(mScrollableParentView != null) {
                        float moveX = getSecureX(ev);
                        float moveY = getSecureY(ev);
                        if(mScrollableParentView instanceof ViewPager){
                            mScrollableParentView.requestDisallowInterceptTouchEvent(true);
                            setClickable(true);
                        }else {
                            if (Math.abs(moveX - downX) > Math.abs(moveY - downY)) {
                                mScrollableParentView.requestDisallowInterceptTouchEvent(true);
                                setClickable(true);
                            } else {
                                mScrollableParentView.requestDisallowInterceptTouchEvent(false);
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if(mScrollableParentView != null) {
                        mScrollableParentView.requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Throwable e) {
            Logger.e(TAG, String.valueOf(e.getMessage()));
            e.printStackTrace();
        }
        return false;
    }

    /**
     * setAutoScrollInterval:设置切换页面的时间. <br/>
     *
     * @param autoScrollIntervalByMillis 滚动时间
     */
    public void setAutoScrollInterval(long autoScrollIntervalByMillis) {
        this.autoScrollInterval = autoScrollIntervalByMillis;
    }

//    /**
//     * setScrollDuration:设置切换页面锁使用的时间，单位毫秒. <br/>
//     *
//     * @param scrollDuration 时间
//     */
//    public void setScrollDuration(int scrollDuration) {
//        this.mDuration = scrollDuration;
//    }

    /**
     * 开始自动滚动. <br/>
     */
    public void startAutoScroll() {
        mHandler.setViewPager(BannerViewPager.this);
        isAutoScrollable = true;
        startAutoScrollInternal();
    }

    /**
     * 开始自动滚动，但是不会设置标记位(开始滚动之前先干掉原来的滚动信息). <br/>
     */
    private void startAutoScrollInternal() {
        if (isAutoScrollable) {
            stopAutoScrollInternal();
            mHandler.sendEmptyMessageDelayed(HANDLE_AUTO_SCROLL, autoScrollInterval);
        }
    }

    /**
     * 停止滚动，状态不会恢复，除非重新调用startAutoScroll()方法. <br/>
     */
    public void stopAutoScroll() {
        mHandler.setViewPager(null);
        isAutoScrollable = false;
        stopAutoScrollInternal();
    }

    /**
     * 停止自动滚动，但是不会设置状态，当手指离开屏幕，可以自动恢复. <br/>
     */
    private void stopAutoScrollInternal() {
        mHandler.removeMessages(HANDLE_AUTO_SCROLL);
    }

    /**
     * 自动滚动到下一页或者第0页. <br/>
     */
    private void autoScroll() {
        if (getAdapter() == null || getAdapter().getCount() <= 1 || !isAutoScrollable) {
            return;
        }
/*
        try {
            int[] screen = new int[2];
            getLocationOnScreen(screen);

            Rect local = new Rect();
            getLocalVisibleRect(local);

            if(screen[1] < 0 && local.top == 0) {
                mHandler.sendEmptyMessageDelayed(HANDLE_AUTO_SCROLL, autoScrollInterval);
                return;
            }
        } catch(Exception e) {
            DebugLog.e(String.valueOf(e.getMessage()));
        }
*/

        int current = getCurrentItem();
        int count = getAdapter().getCount();
        if (current >= 0 && current < count - 1) {
            setCurrentItem(current + 1);
        } else {
            setCurrentItem(0);
        }

        mHandler.sendEmptyMessageDelayed(HANDLE_AUTO_SCROLL, autoScrollInterval);
    }

    /**
     * ViewPager是否正在被触摸. <br/>
     *
     * @return 是否正在被触摸
     */
    public boolean isTouched() {
        return isTouched;
    }

    /**
     * setViewPagerScroller:设置自定义的Scroller. <br/>
     *
     */
    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            scroller = new CustomScroller(getContext(), (Interpolator) interpolatorField.get(null));
            scrollerField.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * getSecureX:安全的得到MotionEvent中的x,得到失败返回0. <br/>
     *
     * @param ev ev
     * @return x
     */
    private float getSecureX(MotionEvent ev) {
        try {
            return ev.getX();
        } catch (Throwable e) {
            if(DEBUG) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * getSecureY:安全的得到MotionEvent中的Y,得到失败返回0. <br/>
     *
     * @param ev ev
     * @return y
     */
    private float getSecureY(MotionEvent ev) {
        try {
            return ev.getY();
        } catch (Throwable e) {
            if(DEBUG) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 自动滚动Handler
     *
     * @author wangheng
     */
    private static class AutoScrollHandler extends Handler {

        private BannerViewPager viewPager;

        public AutoScrollHandler(BannerViewPager pager) {
            this.viewPager = pager;
        }

        public void setViewPager(BannerViewPager viewPager){
            this.viewPager = viewPager;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_AUTO_SCROLL:
                    if(null != viewPager) {
                        viewPager.autoScroll();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 自定义Scroller.
     * @author wangheng
     */
    static class CustomScroller extends Scroller {

        private int mDuration = DURATION_DEFAULT;

        public CustomScroller(Context context, int duration) {
            super(context);
            mDuration = duration;
        }

        public CustomScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

}
