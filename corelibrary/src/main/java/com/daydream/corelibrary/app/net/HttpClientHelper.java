package com.daydream.corelibrary.app.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author gjc
 * @version 1.0.0
 * @since 2018-05-06
 */

public class HttpClientHelper {

    private static HttpClientHelper mInstance;
    private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    private OkHttpClient okHttpClient;


    private HttpClientHelper() {
        /**
         * 构建OkHttpClient
         */
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        /**
         * 设置读取的超时时间
         */
        builder.readTimeout(30, TimeUnit.SECONDS);
        /**
         * 设置写入的超时时间
         */
        builder.writeTimeout(30, TimeUnit.SECONDS);
        /**
         * 设置连接的超时时间
         */
        builder.connectTimeout(30, TimeUnit.SECONDS);
        /**
         * 保持cookie持久化
         */
        builder.cookieJar(new CookieJar() {

            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });
        //确保支持重定向
        builder.followRedirects(true);
        //https支持
        builder.hostnameVerifier(new HostnameVerifier() {
        @Override
             public boolean verify(String hostname, SSLSession session) {
                                 return true;
                             }
         });
        //builder.sslSocketFactory(HttpsUtils.getSslSocketFactory());

        /**
         * 设置log拦截
         */
        builder.addNetworkInterceptor(interceptor);
        /**
         * 公共参数拦截
         */
        //builder.addInterceptor(new CommonParamsInterceptor(new Gson()));

        okHttpClient = builder.build();

    }


    public static HttpClientHelper getInstance() {
        if (mInstance == null) {
            synchronized (HttpClientHelper.class) {
                if (mInstance == null) {
                    mInstance = new HttpClientHelper();
                }
            }
        }
        return mInstance;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
