package com.hogolife.corelibrary.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.daydream.corelibrary.app.adapter.BaseQuickAdapter;
import com.daydream.corelibrary.app.adapter.BaseViewHolder;
import com.daydream.corelibrary.utils.glide.ImageLoader;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.bean.GankListBean;
import com.hogolife.corelibrary.bean.NearlyGankVO;
import com.hogolife.corelibrary.utils.FormatTimeUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 最新干货adapter
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-11
 */

public class NearlyGankAdapter extends BaseQuickAdapter<NearlyGankVO, BaseViewHolder> {

    private OnItemClickListener mListener;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public NearlyGankAdapter(int layoutResId, List<NearlyGankVO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, NearlyGankVO item) {

        Map<String, List<GankListBean>> results = item.getResults();
        Set<Map.Entry<String, List<GankListBean>>> entries = results.entrySet();
        for (Map.Entry<String, List<GankListBean>> bean : entries) {
            List<GankListBean> value = bean.getValue();
            for (GankListBean gank : value) {
                CardView cv_item = holder.getView(R.id.cv_tech_content);
                //title
                holder.setText(R.id.tv_tech_title, gank.getDesc());
                //pic
                if (gank.getImages() != null) {
                    if (gank.getImages().size() > 0) {
                        ImageView iv_piv = holder.getView(R.id.iv_pic);
                        ImageLoader.getInstance().display(gank.getImages().get(0), iv_piv);
                        holder.setVisible(R.id.iv_pic, true);
                    }
                } else {
                    holder.setVisible(R.id.iv_pic, false);
                }
                //author
                holder.setText(R.id.tv_tech_author, gank.getWho() == null ? "无名" : gank.getWho());
                //publish time
                holder.setText(R.id.tv_tech_time, FormatTimeUtils.subStandardTime(gank.getPublishedAt()));

                holder.getView(R.id.ll_tech_content).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClickListener(item, cv_item);
                        }
                    }
                });
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(NearlyGankVO item, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
