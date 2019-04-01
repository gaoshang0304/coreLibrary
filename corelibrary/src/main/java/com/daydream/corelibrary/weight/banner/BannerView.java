package com.daydream.corelibrary.weight.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daydream.corelibrary.R;
import com.daydream.corelibrary.utils.DeviceUtils;
import com.daydream.corelibrary.utils.glide.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页轮播图
 *
 * @author zhangmao
 * @version 4.0.0
 * @since 2015-02-11
 */
public class BannerView extends RelativeLayout implements OnPageChangeListener, OnTouchListener {

    private static final int TIME_INTERVAL = 3000;// 间隔时间3s
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (mIVPoints.size() != 0) {
                    int index = (mVPImages.getCurrentItem() + 1) % mIVPoints.size();
                    if (index < mIVPoints.size()) {
                        mVPImages.setCurrentItem(index);
                    }
                }
                handler.removeMessages(0);
                handler.sendEmptyMessageDelayed(0, TIME_INTERVAL);
            }
        }
    };
    private Context mContext;
    private ViewPager mVPImages;
    private ImageView iv_banner_bottom_bg;
    private List<ImageView> mIVPoints = new ArrayList<ImageView>();

    public BannerView(Context context) {
        super(context);
        initialize(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        mContext = context;
        View.inflate(context, R.layout.layout_banner_view, this);
        mVPImages = (ViewPager) findViewById(R.id.bannerView_vp_images);
        iv_banner_bottom_bg = (ImageView) findViewById(R.id.homeHeader_iv_banner_bottom_bg);
        //2016-12-01 修改lqy
        //mVPImages.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DeviceUtil.getWidth(context) * 255 / 640));
        mVPImages.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DeviceUtils.getScreenWidth(context) * 300 / 750));
        iv_banner_bottom_bg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DeviceUtils.getScreenWidth(context) * 300 / 750));
        mVPImages.setOnPageChangeListener(this);
        mVPImages.setOnTouchListener(this);
    }

    public void setAdapter(PagerAdapter adapter,String url,String swtich) {
        mVPImages.setAdapter(adapter);
        addPoints(adapter.getCount());
        startScroll();
        if ("1".equals(swtich)){
            iv_banner_bottom_bg.setVisibility(VISIBLE);
            ImageLoader.getInstance().display(url, iv_banner_bottom_bg);
        }else {
            iv_banner_bottom_bg.setVisibility(GONE);
        }
    }

    private void addPoints(int size) {
        LinearLayout ll_dots = (LinearLayout) findViewById(R.id.bannerView_ll_dots);
        ll_dots.removeAllViews();
        mIVPoints.clear();
        for (int i = 0; i < size; i++) {
            ImageView ivPoint = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 10, 0);// 这里修改间距(px)
            ivPoint.setLayoutParams(params);
            ll_dots.addView(ivPoint);
            mIVPoints.add(ivPoint);
        }
        updatePoints(0);
    }

    private void updatePoints(int position) {
        for (int i = 0; i < mIVPoints.size(); i++) {
            if (i == position) {// 这里修改图片
                mIVPoints.get(i).setBackgroundResource(R.drawable.banner_indecator_normal);
            } else {
                mIVPoints.get(i).setBackgroundResource(R.drawable.banner_indecator_selected);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float offset, int offsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        updatePoints(position);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mVPImages.requestDisallowInterceptTouchEvent(true);
                stopScroll();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mVPImages.requestDisallowInterceptTouchEvent(false);
                startScroll();
                v.performClick();
                break;
            default:
                break;
        }
        return false;
    }

    private void startScroll() {
        handler.sendEmptyMessageDelayed(0, TIME_INTERVAL);
    }

    private void stopScroll() {
        handler.removeMessages(0);
    }

}
