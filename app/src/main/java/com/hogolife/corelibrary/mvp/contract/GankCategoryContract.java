package com.hogolife.corelibrary.mvp.contract;

import com.daydream.corelibrary.app.mvp.IPresenter;
import com.daydream.corelibrary.app.mvp.IView;
import com.hogolife.corelibrary.bean.GankListBean;

import java.util.List;

/**
 * @author gjc
 * @version ;
 * @since 2018-04-08
 */

public interface GankCategoryContract {

    interface View extends IView {

        void showGankContent(List<GankListBean> list);

        void showGankMoreContent(List<GankListBean> list);

    }

    interface Presenter extends IPresenter<View> {

        void getGankContent(String category, int page);

    }

}
