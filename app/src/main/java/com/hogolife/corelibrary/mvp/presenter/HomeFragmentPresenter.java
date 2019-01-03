package com.hogolife.corelibrary.mvp.presenter;

import android.content.Context;

import com.daydream.corelibrary.app.mvp.RxPresenter;
import com.hogolife.corelibrary.mvp.bean.ResponseResult;
import com.hogolife.corelibrary.mvp.contract.HomeFragmentContract;
import com.hogolife.corelibrary.mvp.manager.DataManager;
import com.hogolife.corelibrary.mvp.manager.RxUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.functions.Consumer;

/**
 * class
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class HomeFragmentPresenter extends RxPresenter<HomeFragmentContract.View> implements HomeFragmentContract.Presenter {

    private final Context mContext;

    public HomeFragmentPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void getPhotoData(@NotNull String category, int page) {
        addSubscribe(DataManager.getInstance(mContext).getPhotoData(category, page)
        .compose(RxUtil.rxSchedulerHelper())
        .subscribe(new Consumer<ResponseResult>() {
            @Override
            public void accept(ResponseResult photoVO) throws Exception {
                mView.showPhoto(photoVO.getResults());
            }
        }));
    }
}
