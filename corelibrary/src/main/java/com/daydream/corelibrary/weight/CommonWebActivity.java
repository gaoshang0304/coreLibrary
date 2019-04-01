package com.daydream.corelibrary.weight;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.daydream.corelibrary.R;
import com.daydream.corelibrary.app.base.BaseToolBarActivity;
import com.daydream.corelibrary.app.extra.LibExtra;
import com.daydream.corelibrary.app.mvp.IPresenter;

/**
 * 通用web activity
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-05-24
 */

public class CommonWebActivity extends BaseToolBarActivity {

    private WebView mWebView;
    private ProgressBar mProgressBar;

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_web;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String extra = getIntent().getStringExtra(LibExtra.url);
        String title = getIntent().getStringExtra(LibExtra.title);

        initTitleBar(toolbar, TextUtils.isEmpty(title) ? "网页" : title);

        mWebView = findViewById(R.id.web_view);
        mProgressBar = findViewById(R.id.progress_bar);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//设置webview支持javascript脚本

        mWebView.setWebViewClient(new CustomWebClient());
        mWebView.setWebChromeClient(new CustomChromeClient());

        if (!TextUtils.isEmpty(extra)) {
            mWebView.loadUrl(extra);
        }
    }

    private class CustomWebClient extends WebViewClient {
        //覆写shouldOverrideUrlLoading实现内部显示网页
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (!url.startsWith("https://") && !url.startsWith("http://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
            mWebView.loadUrl(url);
            return true;
        }

    }

    private class CustomChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            initTitleBar(toolbar, TextUtils.isEmpty(title) ? "网页" : title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mProgressBar.setProgress(newProgress);//设置进度值
                }
            } catch (Exception e) {

            }
        }
    }

    //设置返回键动作（防止按返回键直接退出程序)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                mWebView.goBack();
                return true;
            } else {//当webview处于第一页面时,销毁页面
                onBackPressed();
            }


        }
        return super.onKeyDown(keyCode, event);
    }
}
