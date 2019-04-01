package com.hogolife.corelibrary.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daydream.corelibrary.app.base.BaseFragment;
import com.daydream.corelibrary.app.extra.LibExtra;
import com.daydream.corelibrary.weight.CommonWebActivity;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.adapter.GankListAdapter;
import com.hogolife.corelibrary.bean.GankListBean;
import com.hogolife.corelibrary.constants.Extra;
import com.hogolife.corelibrary.mvp.contract.GankCategoryContract;
import com.hogolife.corelibrary.mvp.presenter.GankPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Tech news
 *
 * @author gjc
 * @version ;;
 * @since 2018-12-13
 */
public class GankNewsFragment extends BaseFragment<GankPresenter> implements GankCategoryContract.View,
        SwipeRefreshLayout.OnRefreshListener, GankListAdapter.OnItemClickListener {

    @BindView(R.id.rv_gankio_girl)
    RecyclerView rvContent;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    private String mCategory = "";
    private int mPage = 1;
    private boolean loadingMore;
    private GankListAdapter mAdapter;
    private List<GankListBean> mBean = new ArrayList<>();

    @Override
    public GankPresenter initPresenter() {
        return new GankPresenter(mContext);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gank_news;
    }

    public static GankNewsFragment newInstance(String category) {
        GankNewsFragment fragment = new GankNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Extra.CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mCategory = arguments.getString(Extra.CATEGORY);
        }
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(layoutManager);
        rvContent.addOnScrollListener(new MyRvScrollListener());
        srlRefresh.setOnRefreshListener(this);
        srlRefresh.setRefreshing(true);
        mAdapter = new GankListAdapter(R.layout.item_gank_data, mBean);
        mAdapter.setOnItemClickListener(this);
        rvContent.setAdapter(mAdapter);

        initData();
    }

    private void initData() {
        mPresenter.getGankContent(mCategory, mPage);
    }

    @Override
    protected void loadData() {
        super.loadData();
        //mPresenter.getGankContent(mCategory, mPage);
    }

    @Override
    public void showGankContent(List<GankListBean> list) {
        mBean.clear();
        mBean.addAll(list);
        if (srlRefresh.isRefreshing()) {
            srlRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showGankMoreContent(List<GankListBean> list) {
        loadingMore = false;
        mBean.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        mPresenter.getGankContent(mCategory, mPage);
    }

    @Override
    public void onItemClickListener(GankListBean item, View view) {
        Intent intent = new Intent(mContext, CommonWebActivity.class);
        intent.putExtra(LibExtra.url, item.getUrl());
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, view, "item").toBundle());
    }

    private class MyRvScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int itemCount = recyclerView.getLayoutManager().getItemCount();
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int position = layoutManager.findLastVisibleItemPosition();
            if (!loadingMore && position == (itemCount - 1)) {
                loadingMore = true;
                mPresenter.getGankContent(mCategory, ++mPage);
            }
        }
    }
}
