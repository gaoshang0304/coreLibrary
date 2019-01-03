package com.daydream.corelibrary.app.loadmore;

import com.daydream.corelibrary.R;
import com.daydream.corelibrary.app.adapter.BaseViewHolder;

/**
 * 自定义loadMoreView
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-05-11
 */

public class CustomLoadMoreView extends LoadMoreView {
    private String endTips;

    @Override
    public int getLayoutId() {
        return R.layout.layout_custom_load_more_view;
    }

    @Override
    public int getLoadingViewId() {
        return R.id.loading_view;
    }

    @Override
    public int getLoadFailViewId() {
        return R.id.load_fail_view;
    }

    @Override
    public int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        super.convert(holder);
        switch (getLoadMoreStatus()) {
            case STATUS_END:
                holder.setText(R.id.tv_load_end_tips, endTips);
                break;
        }
    }


    public String getEndTips() {
        return endTips;
    }

    public void setEndTips(String endTips) {
        this.endTips = endTips;
    }
}