package com.hogolife.corelibrary.mvp.contract;

import com.daydream.corelibrary.app.mvp.IPresenter;
import com.daydream.corelibrary.app.mvp.IView;
import com.hogolife.corelibrary.bean.GankListBean;

import java.util.List;

/**
 * 妹子
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-03
 */

public interface BeautyFragmentContract {

    interface View extends IView {
        void showPhoto(List<GankListBean> list);
    }

    interface Presenter extends IPresenter<View> {
        void getPhotoData(String category, int page);
    }
}
