package com.daydream.corelibrary.weight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.daydream.corelibrary.R;
import com.daydream.corelibrary.photo.photopicker.OnPhotoPickListener;
import com.daydream.corelibrary.photo.photopicker.PhotoPicker;
import com.daydream.corelibrary.photo.photopicker.camera.CameraActivity;
import com.daydream.corelibrary.photo.photopicker.utils.ImageCaptureManager;


/**
 * 选择照片pop
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-05-07
 */

public class SelectPhotoPop extends PopupWindow implements View.OnClickListener {

    private OnPhotoPickListener mListener;
    private Activity mContext;
    private View convertView;
    private TextView tv_photos, tv_camera, tv_cancel;
    private int mTotalNum = 9;


    public SelectPhotoPop(Activity context, OnPhotoPickListener listener) {
        mContext = context;
        mListener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_pop_select_photo, null);
        this.setContentView(convertView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setAnimationStyle(R.style.PopupBottomTranslate);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        this.setOnDismissListener(new PopDismissListener());
        init();
    }

    private void init() {
        tv_photos = convertView.findViewById(R.id.tv_from_photos);
        tv_camera = convertView.findViewById(R.id.tv_from_camera);
        tv_cancel = convertView.findViewById(R.id.tv_cancel);

        addListener();
    }

    private void addListener() {
        tv_photos.setOnClickListener(this);
        tv_camera.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    public void showAtLocation(View parent) {
        setBackgroundAlpha(mContext, 0.5f);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_from_photos) {
            PhotoPicker.init().setMaxCount(mTotalNum).setShowCamera(false).setUseSystemCamera(false)
                    .setPreviewEnable(true).startPick(mContext, mListener);
            dismiss();
        } else if (id == R.id.tv_from_camera) {
            mContext.startActivityForResult(new Intent(mContext, CameraActivity.class)
                    .putExtra(ImageCaptureManager.CAPTURED_PHOTO_NEED_EDIT, true), CameraActivity.REQUEST_CAMERA_CODE);
            dismiss();
        } else if (id == R.id.tv_cancel) {
            dismiss();
        }
    }

    public void setTotalPhoto(int number) {
        mTotalNum = number;
    }

    private class PopDismissListener implements OnDismissListener {
        @Override
        public void onDismiss() {
            setBackgroundAlpha(mContext, 1f);
        }
    }

    /**
     * 设置页面的透明度
     * @param bgAlpha 1表示不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }
}
