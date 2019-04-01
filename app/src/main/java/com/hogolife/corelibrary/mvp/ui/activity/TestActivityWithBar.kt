package com.hogolife.corelibrary.mvp.ui.activity

import android.os.Bundle
import com.daydream.corelibrary.app.base.BaseActivity
import com.hogolife.corelibrary.R
import com.hogolife.corelibrary.mvp.contract.TestContract
import com.hogolife.corelibrary.mvp.presenter.TestPresenter
import kotlinx.android.synthetic.main.activity_test_withbar.*

/**
 *
 *
 * @author gjc
 * @version 1.1.0
 * @since 2019-03-27
 */
class TestActivityWithBar : BaseActivity<TestPresenter>(), TestContract.View {

    override fun initPresenter(): TestPresenter {
        return TestPresenter(this)
    }

    override fun getLayoutId(): Int {
         return R.layout.activity_test_withbar
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {
        //showLoading("")
        mPresenter.getTestContent()
    }

    override fun showTestContent() {
        textView.text = "test result is OK!"
    }
}