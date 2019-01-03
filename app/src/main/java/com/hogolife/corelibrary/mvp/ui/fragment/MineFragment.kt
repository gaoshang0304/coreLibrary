package com.hogolife.corelibrary.mvp.ui.fragment

import android.os.Bundle
import android.view.View
import com.daydream.corelibrary.app.mvp.BaseMvpFragment
import com.hogolife.corelibrary.R
import com.hogolife.corelibrary.mvp.contract.MineFragmentContract
import com.hogolife.corelibrary.mvp.presenter.MineFragmentPresenter

/**
 * 首页fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-12-30
 */
class MineFragment : BaseMvpFragment<MineFragmentPresenter>(), MineFragmentContract.View{

    override fun initPresenter(): MineFragmentPresenter {
        return MineFragmentPresenter(activity)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {

    }

    override fun loadData() {

    }
}