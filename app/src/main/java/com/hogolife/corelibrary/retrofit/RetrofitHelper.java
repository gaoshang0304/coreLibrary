package com.hogolife.corelibrary.retrofit;

import com.daydream.corelibrary.app.net.HttpClientHelper;
import com.google.gson.GsonBuilder;
import com.hogolife.corelibrary.api.GankApis;
import com.hogolife.corelibrary.constants.UrlConstants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-10-09
 */

public class RetrofitHelper {

    private static RetrofitHelper instance = null;
    private GankApis gankService = null;

    public static RetrofitHelper getInstance() {
        if (instance == null) {
            instance = new RetrofitHelper();
        }
        return instance;
    }

    public RetrofitHelper() {
        init();
    }

    private void init() {

        OkHttpClient okHttpClient = HttpClientHelper.getInstance().getOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlConstants.GANK_HOST)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        gankService = retrofit.create(GankApis.class);

    }

    public GankApis getGankService() {
        return gankService;
    }
}
