package com.hogolife.corelibrary.adapter;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.MediaController;

import com.daydream.corelibrary.app.adapter.BaseQuickAdapter;
import com.daydream.corelibrary.app.adapter.BaseViewHolder;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.bean.GankListBean;
import com.hogolife.corelibrary.weight.FullScreenVideo;

import java.util.List;

/**
 * class
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-10
 */

public class VideoAdapter extends BaseQuickAdapter<GankListBean, BaseViewHolder> {

    public VideoAdapter(int layoutResId, @Nullable List<GankListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, GankListBean item) {
        holder.setText(R.id.tv_desc, item.getDesc());
        FullScreenVideo player = holder.getView(R.id.fsv_player);
        String url = item.getUrl();
        Uri uri = Uri.parse(url);
        player.setMediaController(new MediaController(mContext));
        player.setOnCompletionListener(new MyCompletionListener());
        player.setVideoURI(uri);
        int position = getParentPosition(item);
        if (0 == position) {
            player.start();
        }
    }

    private class MyCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {

        }
    }
}
