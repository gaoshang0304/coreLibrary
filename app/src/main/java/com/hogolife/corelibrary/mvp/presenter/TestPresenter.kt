package com.hogolife.corelibrary.mvp.presenter

import android.content.Context
import com.daydream.corelibrary.app.mvp.RxPresenter
import com.hogolife.corelibrary.mvp.contract.TestContract

/**
 * class
 *
 * @author gjc
 * @version 1.1.0
 * @since 2019-03-28
 */
class TestPresenter constructor(val context: Context): RxPresenter<TestContract.View>(), TestContract.Presenter{

    override fun getTestContent() {
        mView?.showTestContent()
    }

}