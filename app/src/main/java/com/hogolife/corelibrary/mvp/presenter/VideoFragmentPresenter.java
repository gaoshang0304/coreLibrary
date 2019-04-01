package com.hogolife.corelibrary.mvp.presenter;

import android.content.Context;

import com.daydream.corelibrary.app.mvp.RxPresenter;
import com.hogolife.corelibrary.bean.ResponseResult;
import com.hogolife.corelibrary.manager.DataManager;
import com.hogolife.corelibrary.manager.RxUtil;
import com.hogolife.corelibrary.mvp.contract.VideoFragmentContract;

import io.reactivex.functions.Consumer;

/**
 * class
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class VideoFragmentPresenter extends RxPresenter<VideoFragmentContract.View> implements VideoFragmentContract.Presenter {

    private final Context mContext;

    public VideoFragmentPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void getVideoData(String category, int page) {
        addSubscribe(DataManager.getInstance(mContext).getCategoryData(category, page)
                .compose(RxUtil.<ResponseResult>rxSchedulerHelper())
                .subscribe(new Consumer<ResponseResult>() {
                    @Override
                    public void accept(ResponseResult result) throws Exception {
                        if (!result.isError()) {
                            mView.setVideoData(result.getResults());
                        }
                    }
                }));
    }
}
