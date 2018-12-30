package com.daydream.corelibrary.app;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;

/**
 * 应用程序对象.
 *
 * @author daydream
 */
public class App {

    /**
     * 渠道号 *
     */
    private String mChannel;

    /**
     * 主线程的Handler对象 *
     */
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private App() {
    }


    /**
     * 得到App的单例对象
     *
     * @return App的单例对象
     */
    public static App getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * 当前是否是Debug模式
     *
     * @return App的单例对象
     */
    public boolean isDebug() {
        return LibraryConfig.getInstance().isDebug();
    }

    /**
     * 得到全局上下文对象
     *
     * @return App的单例对象
     */
    public Context getContext() {
        return LibraryConfig.getInstance().getAppContext();
    }

    /**
     * 得到全局Handler对象
     *
     * @return Handler
     */
    public Handler getHandler() {
        return mHandler;
    }

    /**
     * 得到全局资源类
     *
     * @return Resource
     */

    public Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到布局填充器
     *
     * @return LayoutInflater
     */
    public LayoutInflater getLayoutInflater() {
        return (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 得到String
     *
     * @param stringId stringId
     * @return String
     */
    public String getString(@StringRes int stringId) {
        return getResources().getString(stringId);
    }

    /**
     * 得到应用渠道号
     *
     * @return 渠道号
     */
//    public String getChannel() {
//        if (null == mChannel) {
//            if (isDebug()) {
//                mChannel = "debug";
//            } else {
//                mChannel = PackageUtils.getMetaInfChannel(getContext());
//                if (StringUtils.isNullOrEmpty(mChannel)) {
//                    mChannel = "360";
//                }
//            }
//        }
//        return mChannel;
//    }


    private static final class Singleton {
        private static final App INSTANCE = new App();
    }
}