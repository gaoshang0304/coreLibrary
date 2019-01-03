package com.hogolife.corelibrary.mvp.ui.fragment

import android.os.Bundle
import android.view.View
import com.daydream.corelibrary.app.mvp.BaseMvpFragment
import com.hogolife.corelibrary.R
import com.hogolife.corelibrary.mvp.contract.MiddleFragmentContract
import com.hogolife.corelibrary.mvp.presenter.MiddleFragmentPresenter

/**
 * 首页fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-12-30
 */
class MiddleFragment : BaseMvpFragment<MiddleFragmentPresenter>(), MiddleFragmentContract.View{

    override fun initPresenter(): MiddleFragmentPresenter {
        return MiddleFragmentPresenter(activity)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_middle
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {

    }

    override fun loadData() {

    }
}