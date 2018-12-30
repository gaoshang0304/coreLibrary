package com.daydream.corelibrary.weight.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.daydream.corelibrary.app.App;

import java.util.ArrayList;
import java.util.List;

/**
 * 可无限循环的ViewPager
 *
 * @author wangheng
 */

public class LoopViewPager extends AutoScrollViewPager {
    private static final boolean DEBUG = App.getInstance().isDebug();
    private static final String TAG = "Banner";

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public LoopViewPager(Context context) {
        super(context);
        initView(context,null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        super.addOnPageChangeListener(mOnPagerChangeListener);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.removeOnPageChangeListener(mOnPagerChangeListener);
        super.onDetachedFromWindow();
    }

    private ArrayList<OnPageChangeListener> mListener = new ArrayList<>();

    @Override
    public void addOnPageChangeListener(@NonNull ViewPager.OnPageChangeListener listener) {
//        super.addOnPageChangeListener(listener);
        mListener.add(listener);
    }

    @Override
    public void removeOnPageChangeListener(@NonNull ViewPager.OnPageChangeListener listener) {
//        super.removeOnPageChangeListener(listener);
        mListener.remove(listener);
    }

    private void initView(Context context, AttributeSet attrs) {

    }

    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
//        super.setAdapter(adapter);
        throw new IllegalArgumentException("this method nothing to do,please use setLoopAdapter replace");
    }

    public void setLoopAdapter(LoopPagerAdapter adapter){
        super.setAdapter(new Adapter(adapter));
    }

    // 内部的OnBannerPageChangeListener：为了实现外部onPageChange的回调和Banner的无限循环
    private ViewPager.OnPageChangeListener mOnPagerChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            PagerAdapter adapter = getAdapter();
            if(adapter == null){
                return;
            }
            int count = adapter.getCount();
            if(count <= 1){
                for(ViewPager.OnPageChangeListener listener : mListener){
                    listener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
                return;
            }
            int realIndex;
            try {
                if (position == count - 1 && positionOffset == 0) {
                    realIndex = 1;
                    setCurrentItem(realIndex, false);
                } else if (position == 0 && positionOffset == 0) {
                    realIndex = count - 2;
                    setCurrentItem(realIndex, false);
                }else{
                    realIndex = position;
                }

                for(ViewPager.OnPageChangeListener listener : mListener){
                    listener.onPageScrolled(realIndex,positionOffset,positionOffsetPixels);
                }
            } catch (Exception e) {
                if(DEBUG){
                    e.printStackTrace();
                }
            }
        }

        private int previousIndex = 0;
        @Override
        public void onPageSelected(int position) {
            PagerAdapter adapter = getAdapter();
            if(adapter == null){
                return;
            }
            int count = adapter.getCount();
            if(count <= 1){
                for(ViewPager.OnPageChangeListener listener : mListener){
                    listener.onPageSelected(position);
                }
                return;
            }
            int originCount = count - 2;
            int realIndex;

            if (position == 0) {
                realIndex = originCount - 1;
            } else if (position == count - 1) {
                realIndex = 0;
            } else {
                realIndex = position - 1;
            }

            if(previousIndex != realIndex) {
                for (ViewPager.OnPageChangeListener listener : mListener) {
                    listener.onPageSelected(realIndex);
                }
            }
            previousIndex = realIndex;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            for(ViewPager.OnPageChangeListener listener : mListener){
                listener.onPageScrollStateChanged(state);
            }
        }
    };

    /**
     * Adapter
     *
     * @author wangheng
     */
    private class Adapter<T extends IBannerProtocol> extends PagerAdapter {

        private List<T> mBannerList = null;
        private LoopPagerAdapter<T> mAdapter;

        public Adapter(LoopPagerAdapter<T> adapter) {
            mAdapter = adapter;
            this.mBannerList = adapter.createWrapperList();
        }

        @Override
        public int getCount() {
            if (mBannerList != null) {
                return mBannerList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (mBannerList != null && mBannerList.size() != 0) {
                int pageIndex;
                int len = mBannerList.size();
                if (position == 0) {
                    pageIndex = len - 1;
                } else if (position == len - 1) {
                    pageIndex = 0;
                } else {
                    pageIndex = position - 1;
                }
                try {
                    View view = mAdapter.createView(mBannerList.get(pageIndex),pageIndex);
                    container.addView(view, 0);
                    mAdapter.bindDataToView(view,mBannerList.get(pageIndex),pageIndex);
                    return view;
                } catch (Exception e) {
                    if(DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 外部提供数据列表，ItemView的创建和数据绑定
     *
     * @author wangheng
     */
    public static abstract class LoopPagerAdapter<T extends IBannerProtocol> {

        private List<T> mList = new ArrayList<>();

        public void clearList(){
            mList.clear();
        }

        final List<T> createWrapperList(){
            List<T> list = getOriginList();
            if(list == null || list.size() <= 1){
                return list;
            }
            /*
             * addFirstAndLast:把第一个拷贝并添加到List结尾，并把最后一个拷贝并添加到List开头. <br/>
             * 如：原列表：0,1,2,3，则把3拷贝到0之前并把0拷贝到3之后变成：3,0,1,2,3,0
             */
            T first = list.get(0);
            T last = list.get(list.size() - 1);

            // 最后一个和下标为1的item相等，表示已经添加过了(防止外部调用忘记调用clearList就直接调用此方法)
            if (list.size() > 2 && last == list.get(1)) {
                return mList;
            }

            mList.add(0, last);
            mList.addAll(list);
            mList.add(first);
            return mList;
        }

        public abstract List<T> getOriginList();
        public abstract View createView(T banner, int position);
        public abstract void bindDataToView(View view, T banner, int position);
    }
}
