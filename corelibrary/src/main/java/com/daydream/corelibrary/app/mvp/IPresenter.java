package com.daydream.corelibrary.app.mvp;

/**
 * Created by gjc on 2018/04/02.
 * Presenter基类
 */
public interface IPresenter<T extends IView>{

    void attachView(T view);

    void detachView();
}
