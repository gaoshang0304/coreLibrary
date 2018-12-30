package com.daydream.corelibrary.weight.banner;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.droid.library.app.App;
import com.droid.library.thirdplatform.imageloader.CoreImageLoader;
import com.droid.library.utils.app.PixelUtil;
import com.xinye.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Banner帮助类
 *
 * @author wangheng
 */
public class BannerHelper<T extends IBannerProtocol>
        implements ViewPager.OnPageChangeListener,LifecycleCallback{
    private static final boolean DEBUG = App.getInstance().isDebug();
    private static final String TAG = "Banner";

    /**
     * 开始时的延迟时间 *
     */
    private static final int AUTO_SCROLL_INTERVAL = 3000;

    private FragmentActivity mActivity;
    private Fragment mFragment;
    private LoopViewPager mViewPager;
    private LinearLayout mIndicatorContainer;
    private int width,height;
    private ArrayList<T> mBannerList;
    private int mScrollInterval = AUTO_SCROLL_INTERVAL;

    private IBannerCallback<T> mCallback = null;

    public static class Builder<B extends IBannerProtocol>{
        private int width;
        private int height;
        private FragmentActivity activity;
        private Fragment fragment;
        private LoopViewPager viewPager;
        private LinearLayout indicatorLayout;
        private ArrayList<B> bannerList;
        private int scrollInterval;
        private IBannerCallback<B> callback;

        public Builder<B> width(int width){
            this.width = width;
            return this;
        }
        public Builder<B> height(int height){
            this.height = height;
            return this;
        }
        public Builder<B> activity(FragmentActivity activity){
            this.activity = activity;
            return this;
        }
        public Builder<B> fragment(Fragment fragment){
            this.fragment = fragment;
            return this;
        }
        public Builder<B> viewPager(LoopViewPager viewPager){
            this.viewPager = viewPager;
            return this;
        }
        public Builder<B> indicatorLayout(LinearLayout indicatorLayout){
            this.indicatorLayout = indicatorLayout;
            return this;
        }
        public Builder<B> bannerList(ArrayList<B> bannerList){
            this.bannerList = bannerList;
            return this;
        }
        public Builder<B> scrollInterval(int scrollInterval){
            this.scrollInterval = scrollInterval;
            return this;
        }
        public Builder<B> callback(IBannerCallback<B> callback){
            this.callback = callback;
            return this;
        }

        public BannerHelper<B> build(){
            BannerHelper<B> helper = new BannerHelper<>();
            helper.mActivity = activity;
            helper.mFragment = fragment;
            helper.mBannerList = bannerList;
            helper.width = width;
            helper.height = height;
            helper.mViewPager = viewPager;
            helper.mIndicatorContainer = indicatorLayout;
            helper.mCallback = callback;
            helper.mScrollInterval = scrollInterval;
            return helper;
        }
    }

    @Override
    public void onCreateView() {

        mViewPager.addOnPageChangeListener(BannerHelper.this);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mViewPager.getLayoutParams();
        params.width = width;
        params.height = height;
        mViewPager.setLayoutParams(params);

        onBannerListRequested();
    }



    @Override
    public void onDestroyView() {
        if(mViewPager != null){
            mViewPager.removeOnPageChangeListener(BannerHelper.this);
        }
    }

    @Override
    public void onPause() {
        stopAutoScrollIfNeed();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    private void stopAutoScrollIfNeed() {
        if (mViewPager != null && mBannerList != null && mBannerList.size() > 1) {
            mViewPager.stopAutoScroll();
        }
    }
    private void startAutoScrollIfNeed() {
        if (mViewPager != null && mBannerList != null && mBannerList.size() > 1) {
            mViewPager.startAutoScroll();
        }
    }

    @Override
    public void onResume() {
        startAutoScrollIfNeed();
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden){
            stopAutoScrollIfNeed();
        }else{
            startAutoScrollIfNeed();
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onUserVisibleHint(isVisibleToUser);
            }
        });
    }

    private void onUserVisibleHint(boolean isVisibleToUser) {
        if(mActivity != null && mActivity.isFinishing()){
            return;
        }
        if(mFragment != null && mFragment.isDetached()){
            return;
        }
        if (mViewPager != null && mBannerList != null && mBannerList.size() > 1) {
            if (isVisibleToUser) {
                mViewPager.startAutoScroll();
            } else {
                mViewPager.stopAutoScroll();
            }
        }
    }

    /**
     * onBannerListRequested:BannerList请求完成的回调 <br/>
     *
     */
    private void onBannerListRequested() {

        if (mBannerList == null) {
            mViewPager.setVisibility(View.GONE);
        } else {
            if (mBannerList.size() == 0) {
                mViewPager.setVisibility(View.GONE);
                return;
            }
            // 得到View层中的ViewPager和Indicator容器
            mViewPager.setVisibility(View.VISIBLE);

            // 得到指示器View的布局参数

            mIndicatorContainer.removeAllViews();

            if(mBannerList != null && mBannerList.size() > 1) {
                LinearLayout.LayoutParams indicatorParams = getIndicatorViewLayoutParams();

                int len = mBannerList.size();
                for (int i = 0; i < len; i++) {
                    T banner = mBannerList.get(i);
                    if (banner == null) {
                        continue;
                    }
                    addIndicatorView(indicatorParams, i);
                }
            }

            setViewPagerAttributes();
        }
    }

    /**
     * setViewPagerAttributes:设置ViewPager的属性们. <br/>
     */
    private void setViewPagerAttributes() {
        try {

            mViewPager.setLoopAdapter(new LoopViewPager.LoopPagerAdapter<T>() {
                @Override
                public List<T> getOriginList() {
                    return mBannerList;
                }

                @Override
                public View createView(T banner, final int position) {
                    final ImageView view = new ImageView(mActivity);
                    ViewGroup.LayoutParams bannerParams =
                            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    view.setLayoutParams(bannerParams);
                    view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mCallback != null){
                                mCallback.onBannerItemClick(view,mBannerList.get(position),position);
                            }
                        }
                    });
                    return view;
                }

                @Override
                public void bindDataToView(View view, T banner, int position) {
                    if(view instanceof ImageView){
                        ImageView imageView = (ImageView) view;
                        if(mActivity != null) {
                            CoreImageLoader.getInstance().display(imageView,banner.getBannerImageUrl());
                        }
                    }
                }
            });

            if (mBannerList.size() > 1) {
                mViewPager.setOffscreenPageLimit(mBannerList.size());
                mViewPager.setCurrentItem(1, false);
                mViewPager.setAutoScrollInterval(mScrollInterval);
                mViewPager.startAutoScroll();
            } else if (mBannerList.size() == 1) {
                mViewPager.setCurrentItem(0);
            }
        } catch (Exception e) {
            if(DEBUG) {
                e.printStackTrace();
            }
        }
    }

    /**
     * getIndicatorViewLayoutParams:得到指示器的布局参数. <br/>
     *
     * @return LayoutParams
     */
    private LinearLayout.LayoutParams getIndicatorViewLayoutParams() {
        LinearLayout.LayoutParams indicatorParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        indicatorParams.gravity = Gravity.CENTER;
        indicatorParams.leftMargin = PixelUtil.dip2px(6);
        return indicatorParams;
    }

    /**
     * addIndicatorView:添加指示器View. <br/>
     *
     * @param indicatorParams params
     * @param position position
     */
    private void addIndicatorView(LinearLayout.LayoutParams indicatorParams, final int position) {
        ImageView indicatorView = new ImageView(mActivity);

        indicatorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager != null){
                    mViewPager.setCurrentItem(position);
                }
            }
        });

        indicatorView.setLayoutParams(indicatorParams);
        indicatorView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (0 == position) {
            indicatorView.setImageResource(R.drawable.circle_banner_checked);
        } else {
            indicatorView.setImageResource(R.drawable.circle_banner_normal);
        }
        mIndicatorContainer.addView(indicatorView);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int count = mIndicatorContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView indicatorView = (ImageView) mIndicatorContainer.getChildAt(i);
            if (i == position) {
                indicatorView.setImageResource(R.drawable.circle_banner_checked);
            } else {
                indicatorView.setImageResource(R.drawable.circle_banner_normal);
            }
        }
        if(mCallback != null){
            mCallback.onBannerSelected(mBannerList.get(position),position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
