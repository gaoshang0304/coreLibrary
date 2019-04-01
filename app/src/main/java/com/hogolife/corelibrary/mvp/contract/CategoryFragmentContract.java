package com.hogolife.corelibrary.mvp.contract;

import com.daydream.corelibrary.app.mvp.IPresenter;
import com.daydream.corelibrary.app.mvp.IView;

/**
 * middle fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-04
 */

public interface CategoryFragmentContract {

    interface View extends IView {
    }

    interface Presenter extends IPresenter<View> {
    }

}
