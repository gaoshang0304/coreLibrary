package com.daydream.corelibrary.scan.view;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.daydream.corelibrary.R;
import com.daydream.corelibrary.app.base.BaseActivity;
import com.daydream.corelibrary.scan.camera.CameraManager;
import com.daydream.corelibrary.scan.decoding.CaptureActivityHandler;
import com.daydream.corelibrary.scan.decoding.InactivityTimer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanCodeActivity extends BaseActivity implements Callback, View.OnClickListener {

    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;
    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private boolean vibrate;
    private boolean isOpen = true;  //默认开灯
    private ImageView iv_swtich_light;
    private SurfaceHolder surfaceHolder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_code;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

//        StatusBarUtils.setColor(this, getResources().getColor(R.color.translucent_black_55));
        viewfinderView = findViewById(R.id.scanCode_vv_finder);
        CameraManager.init(getApplication());
        inactivityTimer = new InactivityTimer(this);

        (findViewById(R.id.iv_back)).setOnClickListener(this);
        (findViewById(R.id.toolbar_menu_select_pic)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.toolbar_menu_select_pic) {

        }
    }

    private void resumeScan() {
        surfaceHolder = ((SurfaceView) findViewById(R.id.scanCode_sv_preview)).getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    private void pauseScan() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseScan();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

//    @Override
//    public void onPhotoPick(boolean userCancel, List<String> list) {
//
//    }
//
//    @Override
//    public void onPhotoCapture(String path) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Result result = ScanCodeUtils.scanningImage(path);
//                if (result == null) {
//                    Looper.prepare();
//                    ToastUtils.showToast(mContext, "图片格式有误");
//                    Looper.loop();
//                } else {
//                    // 数据返回
//                    Looper.prepare();
//                    String recode = ScanCodeUtils.recode(result.toString());
//                    goToLocalCode(recode);
//                    Looper.loop();
//                }
//            }
//        }).start();
//    }

    /**
     * 扫码出来的内容
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (!TextUtils.isEmpty(resultString)) {
            Intent intent = new Intent();
            intent.putExtra("content", resultString);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * 处理扫码本地二维码结果
     */
    private void goToLocalCode(String resultString) {

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * 判断字符串是否是数字
     */
    private boolean isNumber(String content) {

        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串
     */
    private String decideString(String content) {

        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            return "数字";
        }
        p = Pattern.compile("[a-zA-Z]");
        m = p.matcher(content);
        if (m.matches()) {
            return "字母";
        }
        p = Pattern.compile("[\u4e00-\u9fa5]");
        m = p.matcher(content);
        if (m.matches()) {
            return "汉字";
        }
        return "啥鸟语";
    }

    /**
     * 二维码 可重复扫描
     */
    private void continuePreview() {
        initCamera(surfaceHolder);
        if (handler != null)
            handler.restartPreviewAndDecode();
    }

}