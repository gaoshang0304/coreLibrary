package com.hogolife.corelibrary.app;

import android.app.Application;

import com.daydream.corelibrary.app.LibraryConfig;
import com.hogolife.corelibrary.BuildConfig;

/**
 * class
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-02
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LibraryConfig.getInstance()
                .setAppContext(this)
                .setDebug(BuildConfig.DEBUG);
    }
}
