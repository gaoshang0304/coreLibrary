package com.hogolife.corelibrary.mvp.manager;

import android.content.Context;

import com.hogolife.corelibrary.mvp.api.GankApis;
import com.hogolife.corelibrary.mvp.bean.ResponseResult;
import com.hogolife.corelibrary.mvp.retrofit.RetrofitHelper;

import io.reactivex.Flowable;

/**
 * class
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class DataManager implements HttpHelper {

    private static DataManager instance;
    private final GankApis gankService;

    public DataManager(Context context) {
        gankService = RetrofitHelper.getInstance().getGankService();
    }

    //由于该对象会被频繁调用，采用单例模式，下面是一种线程安全模式的单例写法
    public static DataManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager(context);
                }
            }
        }
        return instance;
    }


    @Override
    public Flowable<ResponseResult> getPhotoData(String category, int page) {
        return gankService.getGankCategoryData(category, page);
    }
}
