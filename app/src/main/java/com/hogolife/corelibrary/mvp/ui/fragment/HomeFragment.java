package com.hogolife.corelibrary.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.daydream.corelibrary.app.base.BaseFragment;
import com.daydream.corelibrary.app.loadmore.CustomLoadMoreView;
import com.daydream.corelibrary.utils.glide.ImageLoader;
import com.daydream.corelibrary.weight.ItemDecoration;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.adapter.NearlyGankAdapter;
import com.hogolife.corelibrary.bean.GankListBean;
import com.hogolife.corelibrary.bean.NearlyGankVO;
import com.hogolife.corelibrary.mvp.contract.HomeFragmentContract;
import com.hogolife.corelibrary.mvp.presenter.HomeFragmentPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * home fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class HomeFragment extends BaseFragment<HomeFragmentPresenter> implements HomeFragmentContract.View,
        SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srl_refresh;
    @BindView(R.id.rv_photo)
    RecyclerView rvPhoto;
    private NearlyGankAdapter mAdapter;
    private int mPage = 1;
    private final String category = "福利";
    private boolean isLoading = true;
    private GankListBean mItem;
    private ImageView iv_banner;
    private View banner;

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
        //srl_refresh.setRefreshing(true);
        srl_refresh.setOnRefreshListener(this);
        rvPhoto.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new NearlyGankAdapter(R.layout.item_gank_data, new ArrayList<>());
        rvPhoto.setAdapter(mAdapter);
        banner = getLayoutInflater().inflate(R.layout.banner_header, null);
        iv_banner = banner.findViewById(R.id.iv_banner);

        rvPhoto.addItemDecoration(new ItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.dp_5)));
        mAdapter.setLoadMoreView(new CustomLoadMoreView());

        initData();
    }

    private void initData() {
        mPresenter.getNearlyData();
    }

    @Override
    public void onRefresh() {
        mPage = 1;
    }

    @Override
    public void showNearlyData(NearlyGankVO data) {
        if (data != null) {
            Map<String, List<GankListBean>> results = data.getResults();
            List<GankListBean> beauty = results.get("福利");
            ImageLoader.getInstance().display(beauty.get(0).getUrl(), iv_banner);
            mAdapter.addHeaderView(banner);

            mAdapter.addData(data);
        }
    }

}
