package com.daydream.corelibrary.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.daydream.corelibrary.app.dialog.LoadingDialog;


/**
 * use to show or hide loading view
 * Created by gjc.
 */
public class DialogUtils {
    private static LoadingDialog dialog;
    private static LoadingDialog dialogMsg;

    public static void showLoadingDialog(Context context) {
        if (dialog == null) {
            dialog = new LoadingDialog.Builder(context)
                    .setShowMessage(false)
                    .setCancelable(false)
                    .setCancelOutside(false).create();
        }

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void showLoadingDialog(Context context, @Nullable String msg) {
        if (dialogMsg == null) {
            dialogMsg = new LoadingDialog.Builder(context)
                    .setCancelable(false)
                    .setMessage(msg)
                    .setCancelOutside(false).create();
        }

        if (!dialogMsg.isShowing()) {
            dialogMsg.show();
        }
    }

    public static void hideLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        if (dialogMsg != null && dialogMsg.isShowing()) {
            dialogMsg.dismiss();
            dialogMsg = null;
        }

    }

}
