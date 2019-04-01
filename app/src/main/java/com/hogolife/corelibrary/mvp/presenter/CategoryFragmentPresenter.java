package com.hogolife.corelibrary.mvp.presenter;

import android.content.Context;

import com.daydream.corelibrary.app.mvp.RxPresenter;
import com.hogolife.corelibrary.mvp.contract.CategoryFragmentContract;

/**
 * 分类
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class CategoryFragmentPresenter extends RxPresenter<CategoryFragmentContract.View> implements CategoryFragmentContract.Presenter {

    private final Context mContext;

    public CategoryFragmentPresenter(Context context) {
        this.mContext = context;
    }

}
