package com.daydream.corelibrary.photo.imagedit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.daydream.corelibrary.photo.imagedit.core.IMGMode;
import com.daydream.corelibrary.photo.imagedit.core.IMGText;
import com.daydream.corelibrary.photo.imagedit.core.file.IMGAssetFileDecoder;
import com.daydream.corelibrary.photo.imagedit.core.file.IMGDecoder;
import com.daydream.corelibrary.photo.imagedit.core.file.IMGFileDecoder;
import com.daydream.corelibrary.photo.imagedit.core.util.IMGUtils;
import com.daydream.corelibrary.photo.imagedit.extra.Extra;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by felix on 2017/11/14 下午2:26.
 */

public class IMGEditActivity extends IMGEditBaseActivity {

    public final String TAG = "edit_activity";

    @Override
    public void onCreated() {

    }

    @Override
    public Bitmap getBitmap() {
        Intent intent = getIntent();
        if (intent == null) {
            return null;
        }

        Uri uri = intent.getParcelableExtra(Extra.EXTRA_IMAGE_URI);
        Log.e(TAG, "uri == " + uri.toString());
        if (uri == null) {
            return null;
        }

        IMGDecoder decoder = null;

        String path = uri.getPath();
        Log.e(TAG, "path == " + path);
        if (!TextUtils.isEmpty(path)) {
            switch (uri.getScheme()) {
                case "asset":
                    decoder = new IMGAssetFileDecoder(this, uri);
                    break;
                case "file":
                    decoder = new IMGFileDecoder(uri);
                    break;
            }
        }

        if (decoder == null) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inJustDecodeBounds = true;

        decoder.decode(options);

        if (options.outWidth > Extra.MAX_WIDTH) {
            options.inSampleSize = IMGUtils.inSampleSize(Math.round(1f * options.outWidth / Extra.MAX_WIDTH));
        }

        if (options.outHeight > Extra.MAX_HEIGHT) {
            options.inSampleSize = Math.max(options.inSampleSize,
                    IMGUtils.inSampleSize(Math.round(1f * options.outHeight / Extra.MAX_HEIGHT)));
        }

        options.inJustDecodeBounds = false;

        Bitmap bitmap = decoder.decode(options);
        if (bitmap == null) {
            return null;
        }

        return bitmap;
    }

    @Override
    public void onText(IMGText text) {
        mImgView.addStickerText(text);
    }

    @Override
    public void onModeClick(IMGMode mode) {
        IMGMode cm = mImgView.getMode();
        if (cm == mode) {
            mode = IMGMode.NONE;
        }
        mImgView.setMode(mode);
        updateModeUI();

        if (mode == IMGMode.CLIP) {
            setOpDisplay(OP_CLIP);
        }
    }

    @Override
    public void onUndoClick() {
        IMGMode mode = mImgView.getMode();
        if (mode == IMGMode.DOODLE) {
            mImgView.undoDoodle();
        } else if (mode == IMGMode.MOSAIC) {
            mImgView.undoMosaic();
        }
    }

    @Override
    public void onCancelClick() {
        finish();
    }

    @Override
    public void onDoneClick() {
        String path = getIntent().getStringExtra(Extra.EXTRA_IMAGE_SAVE_PATH);
        if (!TextUtils.isEmpty(path)) {
            Bitmap bitmap = mImgView.saveBitmap();
            if (bitmap != null) {
                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream(path);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fout);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (fout != null) {
                        try {
                            fout.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                setResult(RESULT_OK, new Intent().putExtra(Extra.EXTRA_IMAGE_SAVE_PATH, path));
                finish();
                return;
            }
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onCancelClipClick() {
        mImgView.cancelClip();
        setOpDisplay(mImgView.getMode() == IMGMode.CLIP ? OP_CLIP : OP_NORMAL);
    }

    @Override
    public void onDoneClipClick() {
        mImgView.doClip();
        setOpDisplay(mImgView.getMode() == IMGMode.CLIP ? OP_CLIP : OP_NORMAL);
    }

    @Override
    public void onResetClipClick() {
        mImgView.resetClip();
    }

    @Override
    public void onRotateClipClick() {
        mImgView.doRotate();
    }

    @Override
    public void onColorChanged(int checkedColor) {
        mImgView.setPenColor(checkedColor);
    }

}
