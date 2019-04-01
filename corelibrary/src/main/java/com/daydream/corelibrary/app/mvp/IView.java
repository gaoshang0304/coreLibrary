package com.daydream.corelibrary.app.mvp;

/**
 * Created by codeest on 2016/8/2.
 * View基类
 */
public interface IView {

    //显示dialog
    void showLoading(String msg);

    //取消dialog
    void hideLoading();

    //=======  State  =======
    void stateError();

    void stateNoNetWork();

    void stateEmpty();

    void stateLoading();

    void stateMain();
}
