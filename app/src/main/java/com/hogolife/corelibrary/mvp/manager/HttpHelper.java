package com.hogolife.corelibrary.mvp.manager;

import com.hogolife.corelibrary.mvp.bean.ResponseResult;

import io.reactivex.Flowable;

/**
 * class
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public interface HttpHelper {

    /**
     * 获取照片接口
     */
    Flowable<ResponseResult> getPhotoData(String category, int page);
}
