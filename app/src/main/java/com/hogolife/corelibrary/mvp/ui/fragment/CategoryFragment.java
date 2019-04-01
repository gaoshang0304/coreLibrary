package com.hogolife.corelibrary.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.daydream.corelibrary.app.base.BaseFragment;
import com.daydream.corelibrary.weight.xtablayout.XTabLayout;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.adapter.FragmentAdapter;
import com.hogolife.corelibrary.mvp.contract.CategoryFragmentContract;
import com.hogolife.corelibrary.mvp.presenter.CategoryFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * category fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-04
 */

public class CategoryFragment extends BaseFragment<CategoryFragmentPresenter> implements CategoryFragmentContract.View {
    @BindView(R.id.tl_tabs)
    XTabLayout tabLayout;
    @BindView(R.id.vp_fragment)
    ViewPager viewPager;
    private GankNewsFragment allFragment, androidFragment, iosFragment, htmlFragment, exSourceFragment;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private GankNewsFragment relaxFragment, randomFragment, appFragment;

    @Override
    public CategoryFragmentPresenter initPresenter() {
        return new CategoryFragmentPresenter(mActivity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {

        allFragment = new GankNewsFragment();
//        Bundle allBundle = new Bundle();
//        allBundle.putString(Extra.CATEGORY, "all");
//        allFragment.setArguments(allBundle);

        androidFragment = new GankNewsFragment();
//        Bundle androidBundle = new Bundle();
//        androidBundle.putString(Extra.CATEGORY, "Android");
//        androidFragment.setArguments(androidBundle);

        iosFragment = new GankNewsFragment();
//        Bundle iosBundle = new Bundle();
//        iosBundle.putString(Extra.CATEGORY, "iOS");
//        iosFragment.setArguments(iosBundle);

        htmlFragment = new GankNewsFragment();
//        Bundle htmlBundle = new Bundle();
//        htmlBundle.putString(Extra.CATEGORY, "前端");
//        htmlFragment.setArguments(htmlBundle);

        exSourceFragment = new GankNewsFragment();
//        Bundle esBundle = new Bundle();
//        esBundle.putString(Extra.CATEGORY, "拓展资源");
//        exSourceFragment.setArguments(esBundle);

        relaxFragment = new GankNewsFragment();
//        Bundle relaxBundle = new Bundle();
//        relaxBundle.putString(Extra.CATEGORY, "休息视频");
//        relaxFragment.setArguments(relaxBundle);

        randomFragment = new GankNewsFragment();
//        Bundle randomBundle = new Bundle();
//        randomBundle.putString(Extra.CATEGORY, "瞎推荐");
//        randomFragment.setArguments(randomBundle);

        appFragment = new GankNewsFragment();
//        Bundle appBundle = new Bundle();
//        appBundle.putString(Extra.CATEGORY, "App");
//        appFragment.setArguments(appBundle);

        //tabs
        mTitleList.add("all");
        mTitleList.add("Android");
        mTitleList.add("iOS");
        mTitleList.add("前端");
        mTitleList.add("拓展资源");
        mTitleList.add("休息视频");
        mTitleList.add("瞎推荐");
        mTitleList.add("App");

        List<Fragment> list = new ArrayList<>();
        list.add(allFragment);
        list.add(androidFragment);
        list.add(iosFragment);
        list.add(htmlFragment);
        list.add(exSourceFragment);
        list.add(relaxFragment);
        list.add(randomFragment);
        list.add(appFragment);

        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(), list, mTitleList));
        viewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }

}
