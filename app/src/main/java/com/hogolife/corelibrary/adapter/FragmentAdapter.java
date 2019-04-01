package com.hogolife.corelibrary.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.hogolife.corelibrary.mvp.ui.fragment.GankNewsFragment;

import java.util.List;

/**
 * Created by gjc on 2017/12/13.
 * <p>
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {
    private List<String> mTitleList;
    private List<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titleList) {
        super(fm);
        this.fragments = fragments;
        this.mTitleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        String title = mTitleList.get(position);
        return GankNewsFragment.newInstance(title);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
