package com.hogolife.corelibrary.mvp.presenter;

import android.content.Context;

import com.daydream.corelibrary.app.mvp.RxPresenter;
import com.hogolife.corelibrary.mvp.contract.MiddleFragmentContract;

/**
 * class
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class MiddleFragmentPresenter extends RxPresenter<MiddleFragmentContract.View> implements MiddleFragmentContract.Presenter {

    private final Context mContext;

    public MiddleFragmentPresenter(Context context) {
        this.mContext = context;
    }

}
