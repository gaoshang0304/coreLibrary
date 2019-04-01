package com.hogolife.corelibrary.mvp.ui;

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
import com.daydream.corelibrary.app.mvp.RxPresenter;
import com.daydream.corelibrary.app.transitionmode.TransitionMode;
import com.daydream.corelibrary.utils.ToastUtils;
import com.daydream.corelibrary.weight.BottomTabView;
import com.daydream.corelibrary.weight.StatusBarUtils;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.mvp.ui.fragment.BeautyFragment;
import com.hogolife.corelibrary.mvp.ui.fragment.CategoryFragment;
import com.hogolife.corelibrary.mvp.ui.fragment.HomeFragment;
import com.hogolife.corelibrary.mvp.ui.fragment.VideoFragment;

import org.jetbrains.annotations.NotNull;

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
    private CategoryFragment categoryFragment;
    private VideoFragment videoFragment;
    private BeautyFragment beautyFragment;
    private long firstTime = 0;

    @NotNull
    @Override
    public RxPresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        StatusBarUtils.setLightMode(this);
        List<BottomTabView.TabItemView> tabList = new ArrayList<>();
        BottomTabView.TabItemView tab_home = new BottomTabView.TabItemView(mContext, getString(R.string.tab_home),
                R.color.color_tab_normal, R.color.color_tab_selected,
                R.mipmap.ic_new_unckeck, R.mipmap.ic_new_checked);
        BottomTabView.TabItemView tab_category = new BottomTabView.TabItemView(mContext, getString(R.string.tab_category),
                R.color.color_tab_normal, R.color.color_tab_selected,
                R.mipmap.ic_category_uncheck, R.mipmap.ic_category_checked);
        BottomTabView.TabItemView tab_video = new BottomTabView.TabItemView(mContext, getString(R.string.tab_video),
                R.color.color_tab_normal, R.color.color_tab_selected,
                R.mipmap.ic_video_uncheck, R.mipmap.ic_video_checked);
        BottomTabView.TabItemView tab_beauty = new BottomTabView.TabItemView(mContext, getString(R.string.tab_beauty),
                R.color.color_tab_normal, R.color.color_tab_selected,
                R.mipmap.ic_beauty_uncheck, R.mipmap.ic_beauty_checked);
        tabList.add(tab_home);
        tabList.add(tab_category);
        tabList.add(tab_video);
        tabList.add(tab_beauty);
        tabView.setTabItemViews(tabList);
        tabView.setOnTabItemSelectListener(this);
        tabView.setSelectedItem(0);

        homeFragment = new HomeFragment();
        categoryFragment = new CategoryFragment();
        videoFragment = new VideoFragment();
        beautyFragment = new BeautyFragment();

        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.add(R.id.container, homeFragment).commit();
        mCurrentContent = homeFragment;

        setOverridePendingTransitionMode(TransitionMode.FADE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onTabItemSelect(int position) {
        switch (position) {
            case 0:
                switchFragment(homeFragment);
                break;

            case 1:
                switchFragment(categoryFragment);
                break;

            case 2:
                switchFragment(videoFragment);
                break;

            case 3:
                switchFragment(beautyFragment);
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
                    // 停止打印Toast
                    ToastUtils.cancelToast();
                    AppManager.getAppManager().AppExit(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
