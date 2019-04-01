package com.hogolife.corelibrary.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.daydream.corelibrary.app.adapter.BaseQuickAdapter;
import com.daydream.corelibrary.app.adapter.BaseViewHolder;
import com.daydream.corelibrary.utils.glide.ImageLoader;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.bean.GankListBean;

import java.util.ArrayList;

/**
 * class
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class PhotoAdapter extends BaseQuickAdapter<GankListBean, BaseViewHolder> {

    private OnItemLongClickListener mListener;

    public PhotoAdapter(int layoutResId, @Nullable ArrayList<GankListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, GankListBean item) {
        ImageView iv_photo = holder.getView(R.id.iv_photo);
        ImageLoader.getInstance().display(item.getUrl(), iv_photo);

        iv_photo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.OnItemLongClick(item);
                }
                return true;
            }
        });
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(GankListBean item);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mListener = listener;
    }
}
