package com.hogolife.corelibrary.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import com.daydream.corelibrary.app.mvp.BaseMvpFragment;
import com.daydream.corelibrary.photo.photopicker.OnPhotoPickListener;
import com.daydream.corelibrary.photo.photopicker.camera.CameraActivity;
import com.daydream.corelibrary.photo.photopicker.utils.Utils;
import com.daydream.corelibrary.weight.SelectPhotoPop;
import com.hogolife.corelibrary.R;
import com.hogolife.corelibrary.mvp.adapter.SelectPhotoAdapter;
import com.hogolife.corelibrary.mvp.contract.MiddleFragmentContract;
import com.hogolife.corelibrary.mvp.presenter.MiddleFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * middle fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-04
 */

public class MiddleFragment extends BaseMvpFragment<MiddleFragmentPresenter> implements MiddleFragmentContract.View {
    @BindView(R.id.btn_photo)
    Button btnPhoto;
    @BindView(R.id.rv_photo)
    RecyclerView rvPhoto;
    private SelectPhotoAdapter photoAdapter;

    @Override
    public MiddleFragmentPresenter initPresenter() {
        return new MiddleFragmentPresenter(mActivity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_middle;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
        rvPhoto.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        photoAdapter = new SelectPhotoAdapter(R.layout.item_photo, new ArrayList<>());
        rvPhoto.setAdapter(photoAdapter);
    }

    @OnClick(R.id.btn_photo)
    public void onViewClicked() {
        SelectPhotoPop photoPop = new SelectPhotoPop(mActivity, new MyPhotoPickListener());
        photoPop.showAtLocation(rvPhoto);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraActivity.REQUEST_CAMERA_CODE) {
            if (data != null) {
                photoAdapter.addData(data.getStringExtra(Utils.EXTRA_IMAGE));
            }
        }
    }

    private class MyPhotoPickListener implements OnPhotoPickListener {
        @Override
        public void onPhotoPick(boolean userCancel, List<String> list) {
            photoAdapter.addData(list);
        }

        @Override
        public void onPhotoCapture(String path) {
            photoAdapter.addData(path);
        }
    }
}
