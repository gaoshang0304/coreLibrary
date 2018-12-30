/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.daydream.corelibrary.scan.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.hogolife.homanager.R;
import com.hogolife.homanager.common.scan.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;


public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final long ANIMATION_DELAY = 100L;
    private static final int OPAQUE = 0xFF;

    /**
     * 四个边角的长度
     */
    private static final int CORNER_LENGTH_DP = 24;
    private static int CORNER_LENGTH = CORNER_LENGTH_DP * 2;
    /**
     * 四个边角的宽度(px)
     */
    private static final int CORNER_WIDTH_DP = 5;
    private static int CORNER_WIDTH = CORNER_WIDTH_DP * 2;
    /**
     * 字体大小
     */
    private static final int TEXT_SIZE_SP = 12;
    private static int TEXT_SIZE = TEXT_SIZE_SP * 2;
    /**
     * 文字与边框下边的间距
     */
    private static final int TEXT_PADDING_TOP_DP = 30;
    private static int TEXT_PADDING_TOP = TEXT_PADDING_TOP_DP * 2;
    /**
     * 文字行间距
     */
    private static final int TEXT_LINE_SPACING_EXTRA_DP = 8;
    private static int TEXT_LINE_SPACING_EXTRA = TEXT_LINE_SPACING_EXTRA_DP * 2;
    /**
     * 手机的屏幕密度
     */
    private static float density;

    private final Paint paint;
    private final int maskColor;
    private final int resultColor;
    private final int cornerColor;
    private final int frameColor;
    private final int laserColor;
    private final int resultPointColor;
    private Bitmap resultBitmap;
    private int scannerAlpha;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;
    private int mCode = 2;//1条形码  2 二维码

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        density = context.getResources().getDisplayMetrics().density;
        CORNER_LENGTH = (int) (CORNER_LENGTH_DP * density);
        CORNER_WIDTH = (int) (CORNER_WIDTH_DP * density);
        TEXT_SIZE = (int) (TEXT_SIZE_SP * density);
        TEXT_PADDING_TOP = (int) (TEXT_PADDING_TOP_DP * density);
        TEXT_LINE_SPACING_EXTRA = (int) (TEXT_LINE_SPACING_EXTRA_DP * density);

        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint();
        paint.setAntiAlias(true);
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);
        frameColor = resources.getColor(R.color.viewfinder_frame);
        cornerColor = resources.getColor(R.color.viewfinder_corner);
        laserColor = resources.getColor(R.color.viewfinder_laser);
        resultPointColor = resources.getColor(R.color.possible_result_points);
        scannerAlpha = 0;
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
        Rect frame = CameraManager.get().getFramingRect(mCode);
        if (frame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {

            // Draw a two pixel solid black border inside the framing rect
            paint.setColor(frameColor);
            canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
            canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
            canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
            canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);

            // Draw four corners outside the framing rect
            paint.setColor(cornerColor);
            Rect fixedFrame = new Rect(frame.left - CORNER_WIDTH, frame.top - CORNER_WIDTH, frame.right + CORNER_WIDTH, frame.bottom + CORNER_WIDTH);
            canvas.drawRect(fixedFrame.left, fixedFrame.top, fixedFrame.left + CORNER_LENGTH, fixedFrame.top + CORNER_WIDTH, paint);
            canvas.drawRect(fixedFrame.left, fixedFrame.top, fixedFrame.left + CORNER_WIDTH, fixedFrame.top + CORNER_LENGTH, paint);
            canvas.drawRect(fixedFrame.right - CORNER_LENGTH, fixedFrame.top, fixedFrame.right, fixedFrame.top + CORNER_WIDTH, paint);
            canvas.drawRect(fixedFrame.right - CORNER_WIDTH, fixedFrame.top, fixedFrame.right, fixedFrame.top + CORNER_LENGTH, paint);
            canvas.drawRect(fixedFrame.left, fixedFrame.bottom - CORNER_WIDTH, fixedFrame.left + CORNER_LENGTH, fixedFrame.bottom, paint);
            canvas.drawRect(fixedFrame.left, fixedFrame.bottom - CORNER_LENGTH, fixedFrame.left + CORNER_WIDTH, fixedFrame.bottom, paint);
            canvas.drawRect(fixedFrame.right - CORNER_LENGTH, fixedFrame.bottom - CORNER_WIDTH, fixedFrame.right, fixedFrame.bottom, paint);
            canvas.drawRect(fixedFrame.right - CORNER_WIDTH, fixedFrame.bottom - CORNER_LENGTH, fixedFrame.right, fixedFrame.bottom, paint);

            //扫描指示线
            // Draw a red "laser scanner" line through the middle to show decoding is active
//            paint.setColor(laserColor);
//            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
//            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//            int middle = frame.height() / 2 + frame.top;
//            canvas.drawRect(frame.exit_from_right + 2, middle - 1, frame.right - 1, middle + 2, paint);
            drawScanLine(canvas, frame);

            // Draw text below scan frame
            paint.setColor(Color.WHITE);
            paint.setTextSize(TEXT_SIZE);// px
            paint.setAlpha(0xFF);
            paint.setTypeface(Typeface.create("System", Typeface.NORMAL));
            String line = getResources().getString(R.string.scan_code_tip_bar_code_line);
            float w1 = paint.measureText(line);
            canvas.drawText(line, (width - w1) / 2, frame.bottom + TEXT_PADDING_TOP, paint);

            // Draw possible circle
//            Collection<ResultPoint> currentPossible = possibleResultPoints;
//            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
//            if (currentPossible.isEmpty()) {
//                lastPossibleResultPoints = null;
//            } else {
//                possibleResultPoints = new HashSet<ResultPoint>(5);
//                lastPossibleResultPoints = currentPossible;
//                paint.setAlpha(OPAQUE);
//                paint.setColor(resultPointColor);
//                for (ResultPoint point : currentPossible) {
//                    canvas.drawCircle(frame.exit_from_right + point.getX(), frame.top + point.getY(), 6.0f, paint);
//                }
//            }
//            if (currentLast != null) {
//                paint.setAlpha(OPAQUE / 2);
//                paint.setColor(resultPointColor);
//                for (ResultPoint point : currentLast) {
//                    canvas.drawCircle(frame.exit_from_right + point.getX(), frame.top + point.getY(), 3.0f, paint);
//                }
//            }

            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

    public void setCodeViewHight(int code) {
        this.mCode = code;
        this.postInvalidate();
    }

    // 扫描线移动的y
    private int scanLineTop;
    // 扫描线移动速度
    private int SCAN_VELOCITY = 15;
    // 扫描线
    private Bitmap scanLine;

    /**
     * 以下为用渐变线条作为扫描线
     *
     * @param canvas
     * @param frame
     */
    private void drawScanLine(Canvas canvas, Rect frame) {
        scanLine = BitmapFactory.decodeResource(getResources(), R.drawable.ic_wx_scan_line);
        if (scanLineTop == 0) {
            scanLineTop = frame.top;
        }
        if (scanLineTop >= frame.bottom - 30) {
            scanLineTop = frame.top;
        } else {
            scanLineTop += SCAN_VELOCITY;
            // SCAN_VELOCITY可以在属性中设置，默认为5 }
            Rect scanRect = new Rect(frame.left, scanLineTop, frame.right, scanLineTop + 30);
            canvas.drawBitmap(scanLine, null, scanRect, paint);
        }

    }
}