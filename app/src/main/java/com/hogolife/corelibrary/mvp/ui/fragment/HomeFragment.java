package com.hogolife.corelibrary.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daydream.corelibrary.app.adapter.BaseQuickAdapter;
import com.daydream.corelibrary.app.loadmore.CustomLoadMoreView;
import com.daydream.corelibrary.app.mvp.BaseMvpFragment;
import com.daydream.corelibrary.weight.ItemDecoration;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.mvp.adapter.PhotoAdapter;
import com.hogolife.corelibrary.mvp.bean.GankListBean;
import com.hogolife.corelibrary.mvp.contract.HomeFragmentContract;
import com.hogolife.corelibrary.mvp.presenter.HomeFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * home fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class HomeFragment extends BaseMvpFragment<HomeFragmentPresenter> implements HomeFragmentContract.View, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srl_refresh;
    @BindView(R.id.rv_photo)
    RecyclerView rvPhoto;
    private PhotoAdapter mAdapter;
    private int mPage = 1;
    private final String category = "福利";
    private boolean isLoading = true;

    @Override
    public HomeFragmentPresenter initPresenter() {
        return new HomeFragmentPresenter(getActivity());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
        srl_refresh.setRefreshing(true);
        srl_refresh.setOnRefreshListener(this);
        rvPhoto.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new PhotoAdapter(R.layout.item_photo, new ArrayList<>());
        rvPhoto.setAdapter(mAdapter);
        rvPhoto.addItemDecoration(new ItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.dp_5)));
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(this, rvPhoto);

        initData();
    }

    private void initData() {
        mPresenter.getPhotoData(category, mPage);
    }

    @Override
    public void showPhoto(List<GankListBean> photo) {
        if (mPage == 1) {
            mAdapter.setNewData(photo);
            srl_refresh.setRefreshing(false);
        } else {
            mAdapter.addData(photo);
            mAdapter.loadMoreComplete();
        }
        isLoading = false;
    }

    @Override
    public void onLoadMoreRequested() {
        if (!isLoading) {
            mPage++;
            mPresenter.getPhotoData(category, mPage);
            isLoading = true;
        }
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        mPresenter.getPhotoData(category, mPage);
    }
}
