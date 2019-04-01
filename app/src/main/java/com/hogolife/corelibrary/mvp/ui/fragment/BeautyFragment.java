package com.hogolife.corelibrary.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.daydream.corelibrary.app.adapter.BaseQuickAdapter;
import com.daydream.corelibrary.app.base.BaseFragment;
import com.daydream.corelibrary.app.dialog.ConfirmDialog;
import com.daydream.corelibrary.app.loadmore.CustomLoadMoreView;
import com.daydream.corelibrary.utils.ImageUtils;
import com.daydream.corelibrary.weight.ItemDecoration;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.adapter.PhotoAdapter;
import com.hogolife.corelibrary.bean.GankListBean;
import com.hogolife.corelibrary.mvp.contract.BeautyFragmentContract;
import com.hogolife.corelibrary.mvp.presenter.BeautyFragmentPresenter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 妹子 fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class BeautyFragment extends BaseFragment<BeautyFragmentPresenter> implements BeautyFragmentContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srl_refresh;
    @BindView(R.id.rv_photo)
    RecyclerView rvPhoto;
    private PhotoAdapter mAdapter;
    private int mPage = 1;
    private final String category = "福利";
    private boolean isLoading = true;
    private GankListBean mItem;

    @Override
    public BeautyFragmentPresenter initPresenter() {
        return new BeautyFragmentPresenter(getActivity());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
        //使内容占据状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        srl_refresh.setRefreshing(true);
        srl_refresh.setOnRefreshListener(this);
        rvPhoto.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new PhotoAdapter(R.layout.item_photo, new ArrayList<>());
        rvPhoto.setAdapter(mAdapter);
        mAdapter.setOnItemLongClickListener(new PhotoLongClickListener());

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

    private class PhotoLongClickListener implements PhotoAdapter.OnItemLongClickListener {

        @Override
        public void OnItemLongClick(GankListBean item) {
            mItem = item;
            showSaveDialog();
        }
    }

    private void showSaveDialog() {
        ConfirmDialog confirmDialog = new ConfirmDialog(mContext);
        confirmDialog.setContent("保存图片?");
        confirmDialog.setButtonText(R.string.text_cancel, R.string.text_confirm);
        confirmDialog.setOnClickButtonListener(new ConfirmDialog.OnClickButtonListener() {
            @Override
            public void onClickButtonLeft() {

            }

            @Override
            public void onClickButtonRight() {
                //checkPermission();
            }
        });
        confirmDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {

        }
    }

    private void saveImage(GankListBean item) {

        new Thread(new Runnable() {
            Bitmap bitmap = null;

            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(item.getUrl());
                    InputStream is = null;
                    BufferedInputStream bis = null;
                    try {
                        is = url.openConnection().getInputStream();
                        bis = new BufferedInputStream(is);
                        bitmap = BitmapFactory.decodeStream(bis);

                        boolean isSaveSuccess = ImageUtils.saveImageToGallery(mContext, "librarySave", item.get_id(), bitmap);
                        if (isSaveSuccess) {
                            Looper.prepare();
                            Toast.makeText(mContext, "保存图片成功", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(mContext, "保存图片失败，请稍后重试", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
