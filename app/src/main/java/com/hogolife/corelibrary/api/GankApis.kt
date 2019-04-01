package com.hogolife.corelibrary.api

import com.hogolife.corelibrary.bean.NearlyGankVO
import com.hogolife.corelibrary.bean.ResponseResult
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
     * 分类数据
     */
    @GET("data/{category}/20/{page}")
    fun getGankCategoryData(@Path("category") category: String, @Path("page") page: Int): Flowable<ResponseResult>

    /**
     * 最新数据
     */
    @GET("today")
    fun getNearlyGank(): Flowable<NearlyGankVO>

}