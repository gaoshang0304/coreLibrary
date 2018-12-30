package com.daydream.corelibrary.weight.banner;

import android.view.View;


/**
 * BannerCallback.
 *
 * @author wangheng
 */
public interface IBannerCallback<T extends IBannerProtocol> {

    /**
     * onBannerItemClick的Item被点击的时候的回调. <br/>
     *
     * @param itemView itemView
     * @param item item
     * @param position position
     */
    void onBannerItemClick(View itemView, T item, int position);

    void onBannerSelected(T item, int pageIndex);

}
