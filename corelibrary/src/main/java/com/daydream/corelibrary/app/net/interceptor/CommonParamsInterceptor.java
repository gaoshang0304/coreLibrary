package com.daydream.corelibrary.app.net.interceptor;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dayDream on 2018/5/2.
 */

public class CommonParamsInterceptor implements Interceptor {

    private Gson mGson;
    public static final MediaType JSON = MediaType.parse("application/json:charset=utf-8");

    public CommonParamsInterceptor(Gson gson) {
        mGson = gson;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
            //获取到request
            Request request = chain.request();

            //String value = SharePre.getInstance(AppUtils.getContext()).getValue(SharePre.BASIC_TOKEN, "");
            //公共参数hasmap
            try {
//                Request.Builder requestBuilder = request.newBuilder()
//                        .addHeader(Extra.token_header, value)
//                        .addHeader(Extra.content_header, Constants.CONTENT_HEADER);

//                Request newRequest = requestBuilder.build();
//                return chain.proceed(newRequest);
            } catch (Exception e) {

            }
            return chain.proceed(request);
//        } else {
//            showToast("请检查网络");
//        }
//        return null;

    }

}
