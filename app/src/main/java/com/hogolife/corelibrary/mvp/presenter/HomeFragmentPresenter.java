package com.hogolife.corelibrary.mvp.presenter;

import android.content.Context;

import com.daydream.corelibrary.app.mvp.RxPresenter;
import com.hogolife.corelibrary.bean.NearlyGankVO;
import com.hogolife.corelibrary.manager.DataManager;
import com.hogolife.corelibrary.manager.RxUtil;
import com.hogolife.corelibrary.mvp.contract.HomeFragmentContract;

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
    public void getNearlyData() {
        addSubscribe(DataManager.getInstance(mContext).getNearlyGank()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new Consumer<NearlyGankVO>() {
                    @Override
                    public void accept(NearlyGankVO nearlyGankVO) throws Exception {
                        mView.showNearlyData(nearlyGankVO);
                    }
                })
        );
    }
}
