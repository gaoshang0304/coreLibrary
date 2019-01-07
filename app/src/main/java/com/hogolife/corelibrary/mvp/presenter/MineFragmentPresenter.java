package com.hogolife.corelibrary.mvp.presenter;

import android.content.Context;

import com.daydream.corelibrary.app.mvp.RxPresenter;
import com.hogolife.corelibrary.mvp.contract.MineFragmentContract;

/**
 * class
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class MineFragmentPresenter extends RxPresenter<MineFragmentContract.View> implements MineFragmentContract.Presenter {

    private final Context mContext;

    public MineFragmentPresenter(Context context) {
        this.mContext = context;
    }

}
