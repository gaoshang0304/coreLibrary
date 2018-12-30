package com.daydream.corelibrary.weight.banner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.daydream.corelibrary.R;
import com.daydream.corelibrary.app.App;
import com.daydream.corelibrary.utils.DeviceUtils;
import java.util.ArrayList;


/**
 * 调用Banner模块的时候需要给Banner模块设置宽度、高度、数据列表和Banner点击的回调
 *
 * (不要在RecyclerView或者ListView这种可重用Item的View中使用，否则可能Crash；这种场景请使用BannerHelper手动设置)
 *
 *
 * @author wangheng
 */
public class BannerFragment<T extends IBannerProtocol> extends Fragment {

    private static final boolean DEBUG = App.getInstance().isDebug();
    private static final String TAG = "Banner";

    public static final String KEY_WIDTH = "keyWidth";
    public static final String KEY_HEIGHT = "keyHeight";
    public static final String KEY_BANNER_LIST = "keyBannerList";
    /**
     * 开始时的延迟时间 *
     */
    private static final int AUTO_SCROLL_INTERVAL = 3000;
    /**
     * 保存状态的Data的Key *
     */
    private static final String KEY_DATA = "keyBannerFragmentData";
    /**
     * Banner的容器 *
     */
    private LoopViewPager mViewPager;

    /**
     * 指示器View *
     */
    private LinearLayout mIndicatorContainer;

    /**
     * banner 列表 *
     */
    private ArrayList<T> mBannerList;

    private View mRootView = null;

    private static final float RATIO_DEFAULT = 0.5f;

    private Bundle mData;

    private IBannerCallback<T> mCallback = null;

    private BannerHelper<T> mHelper;

    public static <Banner extends IBannerProtocol> BannerFragment<Banner> create(ArrayList<Banner> bannerList){

        int width = DeviceUtils.getScreenWidth(App.getInstance().getContext());
        int height = (int)(width * RATIO_DEFAULT);

        return create(bannerList,width,height);
    }

    public static <Banner extends IBannerProtocol> BannerFragment<Banner> create(ArrayList<Banner> bannerList,
                                                                         int width, int height){
        Bundle bundle = new Bundle();
        bundle.putInt(BannerFragment.KEY_WIDTH, width);
        bundle.putInt(BannerFragment.KEY_HEIGHT, height);
        bundle.putSerializable(BannerFragment.KEY_BANNER_LIST, bannerList);

        BannerFragment<Banner> fragment = new BannerFragment<>();
        fragment.setData(bundle);
        return fragment;
    }

    /**
     * setCallback:设置Banner的事件Callback. <br/>
     *
     * @param callback callback
     */
    public void setCallback(IBannerCallback<T> callback) {
        this.mCallback = callback;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle(KEY_DATA);
            if (bundle != null) {
                mData = bundle;
            }
        }
        int width,height;
        if (mData != null) {
            width = mData.getInt(KEY_WIDTH);
            height = mData.getInt(KEY_HEIGHT);
            if (width == 0 || height == 0) {
                width = DeviceUtils.getScreenWidth(App.getInstance().getContext());
                height = (int)(width * RATIO_DEFAULT);
            }
            mBannerList = (ArrayList<T>) mData.getSerializable(KEY_BANNER_LIST);
        } else {
            width = DeviceUtils.getScreenWidth(App.getInstance().getContext());
            height = (int)(width * RATIO_DEFAULT);
        }

        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_banner, container,false);
            mViewPager = (LoopViewPager) mRootView.findViewById(R.id.bannerViewPager);
            mIndicatorContainer = (LinearLayout) mRootView.findViewById(R.id.bannerIndicatorContainer);
            mHelper = new BannerHelper.Builder<T>()
                    .activity(getActivity())
                    .fragment(this)
                    .bannerList(mBannerList)
                    .callback(mCallback)
                    .width(width)
                    .height(height)
                    .indicatorLayout(mIndicatorContainer)
                    .viewPager(mViewPager)
                    .scrollInterval(AUTO_SCROLL_INTERVAL)
                    .build();
            mHelper.onCreateView();
        }
        return mRootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mData != null) {
            outState.putBundle(KEY_DATA, mData);
        }
    }

    /**
     * setData:设置启动这个Fragment必须的数据. <br/>
     *
     * @param bundle bundle
     */
    public void setData(Bundle bundle) {
        this.mData = bundle;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(mHelper != null){
            mHelper.onDestroyView();
        }

        if (mRootView != null && mRootView.getParent() != null) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }
    }

    @Override
    public void onPause() {
        if(mHelper != null){
            mHelper.onPause();
        }
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mHelper != null){
            mHelper.onResume();
        }
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(mHelper != null){
            mHelper.onHiddenChanged(hidden);
        }
    }


    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mHelper != null){
            mHelper.setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public void onDestroy() {
        if(mHelper != null){
            mHelper.onDestroyView();
        }
        super.onDestroy();
    }
}

