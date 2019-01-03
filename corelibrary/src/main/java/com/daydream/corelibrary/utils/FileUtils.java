package com.daydream.corelibrary.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 文件操作工具类
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-10-22
 */

public class FileUtils {

    public static String setSavePath(Context context, String fileDir, String fileName) {

        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath() + fileDir;
        } else {
            cachePath = context.getCacheDir().getPath() + fileDir;
        }
        File dir = new File(cachePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, fileName);
        return file.getAbsolutePath();
    }
}
