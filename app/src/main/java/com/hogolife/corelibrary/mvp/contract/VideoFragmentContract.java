package com.hogolife.corelibrary.mvp.contract;

import com.daydream.corelibrary.app.mvp.IPresenter;
import com.daydream.corelibrary.app.mvp.IView;
import com.hogolife.corelibrary.bean.GankListBean;

import java.util.List;

/**
 * mine fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-04
 */

public interface VideoFragmentContract {

    interface View extends IView {
        void setVideoData(List<GankListBean> list);
    }

    interface Presenter extends IPresenter<View> {
        void getVideoData(String category, int page);
    }

}
