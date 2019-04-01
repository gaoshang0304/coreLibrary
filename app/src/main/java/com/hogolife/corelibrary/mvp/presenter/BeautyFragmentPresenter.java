package com.hogolife.corelibrary.mvp.presenter;

import android.content.Context;

import com.daydream.corelibrary.app.mvp.RxPresenter;
import com.hogolife.corelibrary.bean.ResponseResult;
import com.hogolife.corelibrary.manager.DataManager;
import com.hogolife.corelibrary.manager.RxUtil;
import com.hogolife.corelibrary.mvp.contract.BeautyFragmentContract;

import org.jetbrains.annotations.NotNull;

import io.reactivex.functions.Consumer;

/**
 * 妹子
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class BeautyFragmentPresenter extends RxPresenter<BeautyFragmentContract.View> implements BeautyFragmentContract.Presenter {

    private final Context mContext;

    public BeautyFragmentPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void getPhotoData(@NotNull String category, int page) {
        addSubscribe(DataManager.getInstance(mContext).getCategoryData(category, page)
        .compose(RxUtil.rxSchedulerHelper())
        .subscribe(new Consumer<ResponseResult>() {
            @Override
            public void accept(ResponseResult photoVO) throws Exception {
                mView.showPhoto(photoVO.getResults());
            }
        }));
    }
}
