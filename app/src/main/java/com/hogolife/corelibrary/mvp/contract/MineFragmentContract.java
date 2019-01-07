package com.hogolife.corelibrary.mvp.contract;

import com.daydream.corelibrary.app.mvp.IPresenter;
import com.daydream.corelibrary.app.mvp.IView;

/**
 * mine fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-04
 */

public interface MineFragmentContract {

    interface View extends IView {
    }

    interface Presenter extends IPresenter<View> {
    }

}
