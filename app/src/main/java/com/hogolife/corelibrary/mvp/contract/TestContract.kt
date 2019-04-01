package com.hogolife.corelibrary.mvp.contract

import com.daydream.corelibrary.app.mvp.IPresenter
import com.daydream.corelibrary.app.mvp.IView

/**
 * class
 *
 * @author gjc
 * @version 1.1.0
 * @since 2019-03-28
 */
interface TestContract {

    interface View: IView {
        fun showTestContent()
    }

    interface Presenter: IPresenter<View>{
        fun getTestContent()
    }
}