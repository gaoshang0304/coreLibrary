package com.daydream.corelibrary.weight.banner;

/**
 * Fragment生命周期回调
 *
 * @author wangheng
 */
public interface LifecycleCallback {
    void onCreateView();

    void onDestroyView();

    void onResume();

    void onPause();

    void onStart();

    void onStop();

    void onHiddenChanged(boolean hidden);

    void setUserVisibleHint(boolean isVisibleToUser);
}