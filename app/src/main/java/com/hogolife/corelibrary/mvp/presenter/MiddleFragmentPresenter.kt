package com.hogolife.corelibrary.mvp.presenter

import android.app.Activity
import com.daydream.corelibrary.app.mvp.RxPresenter
import com.hogolife.corelibrary.mvp.contract.MiddleFragmentContract

/**
 * 首页presenter
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-12-30
 */
class MiddleFragmentPresenter : RxPresenter<MiddleFragmentContract.View>, MiddleFragmentContract.Presenter {

    private var mActivity: Activity?

    constructor(activity: Activity?) {
        this.mActivity = activity
    }

}