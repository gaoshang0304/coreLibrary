package com.hogolife.corelibrary.mvp.api

import com.hogolife.corelibrary.mvp.bean.ResponseResult
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 干货接口
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */
interface GankApis {

    /**
     * 获取照片
     */
    /**
     * 分类数据
     */
    @GET("data/{category}/20/{page}")
    fun getGankCategoryData(@Path("category") category: String, @Path("page") page: Int): Flowable<ResponseResult>

}