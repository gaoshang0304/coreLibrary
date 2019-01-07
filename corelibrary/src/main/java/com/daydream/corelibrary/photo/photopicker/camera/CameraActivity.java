package com.daydream.corelibrary.photo.photopicker.camera;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daydream.corelibrary.R;
import com.daydream.corelibrary.photo.imagedit.IMGEditActivity;
import com.daydream.corelibrary.photo.photopicker.fragment.ImagePagerFragment;
import com.daydream.corelibrary.photo.photopicker.utils.ImageCaptureManager;
import com.daydream.corelibrary.photo.photopicker.utils.Utils;
import com.daydream.corelibrary.photo.photopicker.utils.camera.ImageUtils;
import com.daydream.corelibrary.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.daydream.corelibrary.photo.imagedit.extra.Extra.*;


/**
 * Created by changyou on 2016/1/24.
 * 拍照的activity
 */
public class CameraActivity extends AppCompatActivity {

    private CameraRender cameraRender;

    private LinearLayout surfaceContainer;
    private Button takePicture;
    private ImageView flashBtn;
    private ImageView changeBtn;
    private View focusIndex;
    private View bottomBar;
    private String imagePath;

    private Handler handler = new Handler();
    private ProgressDialog dialog;
    private int dialogId = 5;

    private Bitmap bitmap;
    private boolean needEdit;
    public static final int REQUEST_CAMERA_CODE = 1201;
    private final int REQUEST_EDIT_CODE = 122;
    private ImagePagerFragment imagePagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__picker_activity_camera);
        needEdit = getIntent().getBooleanExtra(ImageCaptureManager.CAPTURED_PHOTO_NEED_EDIT, false);
        initView();
        initEvent();
        initCameraRender();
    }

    private void initView() {
        surfaceContainer = (LinearLayout) findViewById(R.id.surfaceView_container);
        takePicture = (Button) findViewById(R.id.takepicture);
        flashBtn = (ImageView) findViewById(R.id.flashBtn);
        changeBtn = (ImageView) findViewById(R.id.change);
        focusIndex = findViewById(R.id.focus_index);
        bottomBar = findViewById(R.id.bottom_bar);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) takePicture.getLayoutParams();
        if (params == null) {
            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.bottomMargin = Utils.getNavigationBarHeight(this) + (int) (getResources().getDisplayMetrics().density * 20);
        takePicture.setLayoutParams(params);


        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) bottomBar.getLayoutParams();
        if (params2 == null) {
            params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params2.bottomMargin = Utils.getNavigationBarHeight(this) + (int) (getResources().getDisplayMetrics().density * 20);
        bottomBar.setLayoutParams(params2);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (bottomBar.getVisibility() == View.VISIBLE) {
            cameraRender.reset();
            bottomBar.setVisibility(View.GONE);
            takePicture.setVisibility(View.VISIBLE);
            return;
        }
        super.onBackPressed();
    }

    private void initEvent() {
        //拍照
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraRender.takePicture();
            }
        });
        //闪光灯
        flashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraRender.turnLight();
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraRender.switchCamera();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraRender.reset();
                bottomBar.setVisibility(View.GONE);
                takePicture.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePath = saveToSDCard(bitmap);
                setFinish();
            }
        });

    }

    private void initCameraRender() {
        cameraRender = new CameraRender(this, surfaceContainer);
        cameraRender.setCameraListener(new CameraListener() {
            @Override
            public void onFlashLigChange(boolean show, String mode) {
                if (show) {
                    flashBtn.setVisibility(View.VISIBLE);
                    if (Camera.Parameters.FLASH_MODE_OFF.equals(mode)) {//关闭状态
                        flashBtn.setImageResource(R.drawable.__picker_camera_flash_off);
                    } else if (Camera.Parameters.FLASH_MODE_TORCH.equals(mode)) {//开启状态
                        flashBtn.setImageResource(R.drawable.__picker_camera_flash_on);
                    } else if (Camera.Parameters.FLASH_MODE_AUTO.equals(mode)) {
                        flashBtn.setImageResource(R.drawable.__picker_camera_flash_auto);
                    }
                } else {
                    flashBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCameraChange(boolean canSwitch, int cameraId) {
                if (canSwitch) {
                    changeBtn.setVisibility(View.VISIBLE);
                } else {
                    changeBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFocusIndex(float x, float y) {
                RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(focusIndex.getLayoutParams());
                layout.setMargins((int) x - 60, (int) y - 60, 0, 0);
                focusIndex.setLayoutParams(layout);
                focusIndex.setVisibility(View.VISIBLE);
                ScaleAnimation sa = new ScaleAnimation(1.5f, 1f, 1.5f, 1f,
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                sa.setDuration(500);
                focusIndex.startAnimation(sa);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        focusIndex.setVisibility(View.INVISIBLE);
                    }
                }, 500);
            }
        });
        cameraRender.setTakePictureCallback(new TakePictureCallback() {
            @Override
            public void prepareTake() {
//                showDialog(dialogId);
            }

            @Override
            public void onTake(byte[] data) {
                toBitmap(data);
                finishTake();
            }

            @Override
            public void onTake(Bitmap bitmap) {
                CameraActivity.this.bitmap = bitmap;
                finishTake();
            }
        });
        cameraRender.create();
    }

    private void finishTake() {
//        dismissDialog(dialogId);
        if (bitmap == null) {
            cameraRender.reset();
            Toast.makeText(CameraActivity.this, R.string.camera_error, Toast.LENGTH_SHORT);
        } else {
            bottomBar.setVisibility(View.VISIBLE);
            takePicture.setVisibility(View.GONE);
        }
    }

    private void setFinish() {
        if (!TextUtils.isEmpty(imagePath)) {
            if (needEdit) {
                String edit_pic = FileUtils.setSavePath(this, "edit_pic", generateFileName());
                Uri uri = Uri.fromFile(new File(imagePath));
                Intent intent = new Intent(this, IMGEditActivity.class);
                intent.putExtra(EXTRA_IMAGE_URI, uri);
                intent.putExtra(EXTRA_IMAGE_SAVE_PATH, edit_pic);
                startActivityForResult(intent, REQUEST_EDIT_CODE);
            } else {
                Intent intent = new Intent();
                intent.putExtra(Utils.EXTRA_IMAGE, imagePath);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private String generateFileName() {
        return UUID.randomUUID().toString()+".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String extra = data.getStringExtra(EXTRA_IMAGE_SAVE_PATH);
                if (!TextUtils.isEmpty(extra)) {
                    Intent intent = new Intent();
                    intent.putExtra(Utils.EXTRA_IMAGE, extra);
                    Log.e("camera", "camera == " + extra);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }
    }

    /**
     * 将拍下来的照片存放在SD卡中
     *
     * @param data
     * @throws IOException
     */
    public void toBitmap(byte[] data) {
        try {
            //获得图片大小
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            Camera.Size adapterSize = cameraRender.getAdapterSize();
            Camera.Size previewSize = cameraRender.getPreviewSize();
            int ratioH = adapterSize.height / previewSize.height;
            int ratioW = adapterSize.width / previewSize.width;
            options.inSampleSize = Math.min(ratioH, ratioW);
            int orientation = cameraRender.getisplayOrientation();
            Matrix matrix = new Matrix();
            matrix.setRotate(orientation);
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            bitmap = Bitmap.createBitmap(BitmapFactory.decodeByteArray(data, 0, data.length, options),
                    0, 0, previewSize.width, previewSize.height, matrix, true);
        } catch (Exception e) {

        }
    }

    public String saveToSDCard(Bitmap bitmap) {
        if (bitmap == null) {
            return "";
        }
        return ImageUtils.saveToFile(this, bitmap);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(getString(R.string.deal_pic));
        }
        return dialog;
    }
}
