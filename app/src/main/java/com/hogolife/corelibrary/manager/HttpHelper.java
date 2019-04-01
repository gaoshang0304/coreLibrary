package com.hogolife.corelibrary.manager;

import com.hogolife.corelibrary.bean.NearlyGankVO;
import com.hogolife.corelibrary.bean.ResponseResult;

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
     * 获取干货分类接口
     */
    Flowable<ResponseResult> getCategoryData(String category, int page);

    /**
     * 获取最新一天干货
     */
    Flowable<NearlyGankVO> getNearlyGank();

}
