package com.hogolife.corelibrary.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.daydream.corelibrary.app.mvp.BaseMvpFragment;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.mvp.contract.MineFragmentContract;
import com.hogolife.corelibrary.mvp.presenter.MineFragmentPresenter;

/**
 * mine fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-04
 */

public class MineFragment extends BaseMvpFragment<MineFragmentPresenter> implements MineFragmentContract.View {
    @Override
    public MineFragmentPresenter initPresenter() {
        return new MineFragmentPresenter(mActivity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {

    }
}
