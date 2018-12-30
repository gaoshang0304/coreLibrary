package com.daydream.corelibrary.weight.banner;

import android.os.Looper;

import java.util.ArrayList;

/**
 * 生命周期委托
 *
 * @author wangheng
 */
public class LifecycleDelegate {

    private ArrayList<LifecycleCallback> mCallbackList = new ArrayList<>();

    public LifecycleDelegate(){

    }

    public synchronized void addLifecycleCallback(LifecycleCallback callback){
        // 只允许在主线程添加
        if(Looper.myLooper() != Looper.getMainLooper()){
            return;
        }
        if(callback == null){
            return;
        }

        if(mCallbackList == null){
            mCallbackList = new ArrayList<>();
        }

        if(mCallbackList.contains(callback)){
            return;
        }

        mCallbackList.add(callback);
    }

    public synchronized void removeLifecycleCallback(LifecycleCallback callback){
        // 只允许在主线程添加
        if(Looper.myLooper() != Looper.getMainLooper()){
            return;
        }
        if(callback == null){
            return;
        }
        if(mCallbackList != null && mCallbackList.contains(callback)){
            mCallbackList.remove(callback);
        }
    }

    public synchronized void onCreateView() {
        if(!hasCallback()){
            return;
        }
        for(LifecycleCallback callback : mCallbackList){
            callback.onCreateView();
        }

    }

    public synchronized void onDestroyView() {
        if(!hasCallback()){
            return;
        }
        for(LifecycleCallback callback : mCallbackList){
            callback.onDestroyView();
        }
    }

    public synchronized void onStart() {
        if(!hasCallback()){
            return;
        }
        for(LifecycleCallback callback : mCallbackList){
            callback.onStart();
        }
    }

    public synchronized void onResume() {
        if(!hasCallback()){
            return;
        }
        for(LifecycleCallback callback : mCallbackList){
            callback.onResume();
        }
    }

    public synchronized void onPause() {
        if(!hasCallback()){
            return;
        }
        for(LifecycleCallback callback : mCallbackList){
            callback.onPause();
        }
    }

    public synchronized void onStop() {
        if(!hasCallback()){
            return;
        }
        for(LifecycleCallback callback : mCallbackList){
            callback.onStop();
        }
    }

    public synchronized void onHiddenChanged(boolean hidden) {
        if(!hasCallback()){
            return;
        }
        for(LifecycleCallback callback : mCallbackList){
            callback.onHiddenChanged(hidden);
        }
    }

    public synchronized void setUserVisibleHint(boolean userVisibleHint) {
        if(!hasCallback()){
            return;
        }
        for(LifecycleCallback callback : mCallbackList){
            callback.setUserVisibleHint(userVisibleHint);
        }
    }

    public synchronized void onDestroy() {
        if(!hasCallback()){
            return;
        }
        mCallbackList.clear();
    }

    private boolean hasCallback(){
        return mCallbackList != null && mCallbackList.size() > 0;
    }


}
