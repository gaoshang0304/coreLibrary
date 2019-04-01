package com.hogolife.corelibrary.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.daydream.corelibrary.app.base.BaseFragment;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.bean.GankListBean;
import com.hogolife.corelibrary.mvp.contract.VideoFragmentContract;
import com.hogolife.corelibrary.mvp.presenter.VideoFragmentPresenter;
import com.hogolife.corelibrary.mvp.ui.activity.TestActivityWithBar;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * video fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-04
 */

public class VideoFragment extends BaseFragment<VideoFragmentPresenter> implements VideoFragmentContract.View {

    private final String category = "休息视频";
    @BindView(R.id.button1)
    Button button1;
    Unbinder unbinder;

    @Override
    public VideoFragmentPresenter initPresenter() {
        return new VideoFragmentPresenter(mActivity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setVideoData(List<GankListBean> list) {

    }

    @OnClick(R.id.button1)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                startActivity(new Intent(mContext, TestActivityWithBar.class));
                break;
        }
    }
}
