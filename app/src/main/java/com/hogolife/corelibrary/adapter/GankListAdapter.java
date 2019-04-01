package com.hogolife.corelibrary.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.daydream.corelibrary.app.adapter.BaseQuickAdapter;
import com.daydream.corelibrary.app.adapter.BaseViewHolder;
import com.daydream.corelibrary.utils.glide.ImageLoader;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.bean.GankListBean;
import com.hogolife.corelibrary.utils.FormatTimeUtils;

import java.util.List;

/**
 * 干活集中营 数据列表
 *
 * @author gjc
 * @version ;
 * @since 2018-04-08
 */
public class GankListAdapter extends BaseQuickAdapter<GankListBean, BaseViewHolder> {

    private OnItemClickListener mListener;

    public GankListAdapter(@LayoutRes int layoutResId, @Nullable List<GankListBean> data) {
        super(layoutResId, data);
    }

    public void addMoreData(List<GankListBean> list) {
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder holder, final GankListBean item) {
        CardView cv_item = holder.getView(R.id.cv_tech_content);
        //title
        holder.setText(R.id.tv_tech_title, item.getDesc());
        //pic
        if (item.getImages() != null) {
            if (item.getImages().size() > 0) {
                ImageView iv_piv = holder.getView(R.id.iv_pic);
                ImageLoader.getInstance().display(item.getImages().get(0), iv_piv);
                holder.setVisible(R.id.iv_pic, true);
            }
        } else {
            holder.setVisible(R.id.iv_pic, false);
        }
        //author
        holder.setText(R.id.tv_tech_author, item.getWho() == null ? "无名" : item.getWho());
        //publish time
        holder.setText(R.id.tv_tech_time, FormatTimeUtils.subStandardTime(item.getPublishedAt()));

        holder.getView(R.id.ll_tech_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClickListener(item, cv_item);
                }
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClickListener(GankListBean item, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
