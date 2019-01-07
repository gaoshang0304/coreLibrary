package com.hogolife.corelibrary.mvp.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.daydream.corelibrary.app.adapter.BaseQuickAdapter;
import com.daydream.corelibrary.app.adapter.BaseViewHolder;
import com.daydream.corelibrary.utils.glide.ImageLoader;
import com.hogolife.corelibrary.R;

import java.util.ArrayList;

/**
 * 选择图片
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class SelectPhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private OnItemClickListener mListener;

    public SelectPhotoAdapter(int layoutResId, @Nullable ArrayList<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        ImageView iv_photo = holder.getView(R.id.iv_photo);
        ImageLoader.getInstance().display(item, iv_photo);

        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemClick(item);
                }
            }
        });
    }

    public interface OnItemClickListener {
        void OnItemClick(String item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
