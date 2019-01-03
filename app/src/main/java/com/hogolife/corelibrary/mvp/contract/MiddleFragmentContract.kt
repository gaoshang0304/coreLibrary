package com.hogolife.corelibrary.mvp.contract

import com.daydream.corelibrary.app.mvp.IPresenter
import com.daydream.corelibrary.app.mvp.IView

/**
 * 首页contract
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-12-30
 */
interface MiddleFragmentContract {

    interface View : IView {
        //fun showPhoto(photo : PhotoVO)
    }

    interface Presenter : IPresenter<View> {
        //fun getPhotoData(page : Int, pageNum : Int)
    }
}