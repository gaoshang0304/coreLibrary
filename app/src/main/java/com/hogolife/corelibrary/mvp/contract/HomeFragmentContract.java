package com.hogolife.corelibrary.mvp.contract;

import com.daydream.corelibrary.app.mvp.IPresenter;
import com.daydream.corelibrary.app.mvp.IView;
import com.hogolife.corelibrary.mvp.bean.GankListBean;

import java.util.List;

/**
 * 首页
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-03
 */

public interface HomeFragmentContract {

    interface View extends IView {
        void showPhoto(List<GankListBean> list);
    }

    interface Presenter extends IPresenter<View> {
        void getPhotoData(String category, int page);
    }
}
