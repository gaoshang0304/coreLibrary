package com.hogolife.corelibrary;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.daydream.corelibrary.app.base.BaseActivity;
import com.daydream.corelibrary.app.manager.AppManager;
import com.daydream.corelibrary.utils.ToastUtils;
import com.daydream.corelibrary.weight.BottomTabView;
import com.hogolife.corelibrary.mvp.ui.fragment.HomeFragment;
import com.hogolife.corelibrary.mvp.ui.fragment.MiddleFragment;
import com.hogolife.corelibrary.mvp.ui.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * class
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class MainActivity extends BaseActivity implements BottomTabView.OnTabItemSelectListener {


    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.tab_view)
    BottomTabView tabView;
    private FragmentTransaction mTransaction;
    private Fragment mCurrentContent;
    private HomeFragment homeFragment;
    private MiddleFragment middleFragment;
    private MineFragment mineFragment;
    private long firstTime = 0;
    private float mOriginalY = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        List<BottomTabView.TabItemView> tabList = new ArrayList<>();
        BottomTabView.TabItemView tab_home = new BottomTabView.TabItemView(mContext, getString(R.string.tab_home),
                R.color.color_tab_normal, R.color.color_tab_selected,
                R.mipmap.ic_home_uncheck, R.mipmap.ic_home_checked);
        BottomTabView.TabItemView tab_middle = new BottomTabView.TabItemView(mContext, getString(R.string.tab_middle),
                R.color.color_tab_normal, R.color.color_tab_selected,
                R.mipmap.ic_discovery_uncheck, R.mipmap.ic_discovery_checked);
        BottomTabView.TabItemView tab_mine = new BottomTabView.TabItemView(mContext, getString(R.string.tab_mine),
                R.color.color_tab_normal, R.color.color_tab_selected,
                R.mipmap.ic_mine_uncheck, R.mipmap.ic_mine_checked);
        tabList.add(tab_home);
        tabList.add(tab_middle);
        tabList.add(tab_mine);
        tabView.setTabItemViews(tabList);
        tabView.setOnTabItemSelectListener(this);
        tabView.setSelectedItem(0);

        homeFragment = new HomeFragment();
        middleFragment = new MiddleFragment();
        mineFragment = new MineFragment();

        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.add(R.id.container, homeFragment).commit();
        mCurrentContent = homeFragment;
    }

    @Override
    public void onTabItemSelect(int position) {
        switch (position) {
            case 0:
                switchFragment(homeFragment);
                break;

            case 1:
                switchFragment(middleFragment);
                break;

            case 2:
                switchFragment(mineFragment);
                break;
        }
    }

    private void switchFragment(Fragment fragment) {
        if (mCurrentContent != null) {
            mTransaction = getSupportFragmentManager().beginTransaction();
            if (!fragment.isAdded()) {
                //  mTransaction.setPrimaryNavigationFragment(fragment);
                mTransaction.hide(mCurrentContent).add(R.id.container, fragment);
            } else {
                mTransaction.hide(mCurrentContent).show(fragment);
            }
            mTransaction.commit();
            mCurrentContent = fragment;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 再按一次退出应用
            long secondTime = System.currentTimeMillis();
            if ((System.currentTimeMillis() - firstTime) > 1000) {
                ToastUtils.showToast(mContext, getString(R.string.exit_app));
                firstTime = secondTime;
            } else {
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 停止打印Toast
                ToastUtils.cancelToast();
                AppManager.getAppManager().AppExit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
