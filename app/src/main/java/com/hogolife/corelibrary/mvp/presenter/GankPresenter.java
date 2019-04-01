package com.hogolife.corelibrary.mvp.presenter;

import android.content.Context;

import com.daydream.corelibrary.app.mvp.RxPresenter;
import com.hogolife.corelibrary.bean.ResponseResult;
import com.hogolife.corelibrary.manager.DataManager;
import com.hogolife.corelibrary.manager.RxUtil;
import com.hogolife.corelibrary.mvp.contract.GankCategoryContract;

import io.reactivex.functions.Consumer;

/**
 * @author gjc
 * @version ;
 * @since 2018-04-08
 */

public class GankPresenter extends RxPresenter<GankCategoryContract.View> implements GankCategoryContract.Presenter {

    private Context mContext;

    public GankPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void getGankContent(String category, final int page) {
        addSubscribe(DataManager.getInstance(mContext).getCategoryData(category, page)
        .compose(RxUtil.<ResponseResult>rxSchedulerHelper())
        .subscribe(new Consumer<ResponseResult>() {

            @Override
            public void accept(ResponseResult result) throws Exception {
                if (!result.isError()) {
                    if (1 == page) {
                        mView.showGankContent(result.getResults());
                    } else {
                        mView.showGankMoreContent(result.getResults());
                    }
                }
            }
        }));

    }

}
